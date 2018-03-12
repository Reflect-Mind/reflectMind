package cn.edu.lingnan.service.command;

import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.sdk.Container.PhaseContainerImpl;
import cn.edu.lingnan.sdk.algorithms.ahoCorasick.AhoCorasick;
import cn.edu.lingnan.sdk.algorithms.ahoCorasick.AhoCorasickImpl;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import cn.edu.lingnan.utils.R;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.IndexRange;
import javafx.util.Pair;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/2/18.
 * 关键词高亮命令
 */
public class TextWorkspaceCommand extends AbstractCommand {

    //访：,受的模式匹配
    private final String BEGIN_PATTERN = "[访受][:：]";//访：

    //括号匹配
    private final String PR_PATTERN = "(\\(.*?\\))|(（.*?）)";

    private final Pattern PATTERN = Pattern.compile(
            PR_PATTERN
            + "|(" + BEGIN_PATTERN + ")"
    );
    //用于格式化文本区域的关键字
    private AhoCorasick customMather = new AhoCorasickImpl(){
        {
            this.append("访：");
            this.append("访:");
            this.append("受：");
            this.append("受:");
        }
    };
    //非换行结尾字符
    public final static String NONE_LINE_CHARS = ",，。.）)}]’'、?》>\"";
    //受:所在的行序号范围 Integer表示访开始的行号 IndexRange: start (included) end (excluded)
    private ObservableList<Pair<Integer, IndexRange>> answers = R.getConfig().getAnswers();
    //访：所在的行序号范围 IndexRange: start (included) end (excluded)
    private ObservableList<Pair<Integer, IndexRange>> asks = R.getConfig().getAsks();
    //匹配到单词
    private List<String> words = R.getConfig().getWords();
    //单独线程执行者
    private ExecutorService executors = Executors.newSingleThreadExecutor();
    //获取心理词汇的服务类
    private VocabService vocabService = new VocabServiceImpl();


    /**
     * 更新目标匹配字段:
     * 将数据库中的心理词汇加载到ac自动机中，以进行心理
     * 词汇的匹配分析
     */
    public void updateAhoMatchingData(){
        executors.execute(() -> {
            AhoCorasick ahoCorasick = R.getConfig().getAhoCorasick();
            this.vocabService
                    .findAllPsyChoVocab()
                    .forEach((vocab -> ahoCorasick.append(vocab)));
            ahoCorasick.append("自信");
            ahoCorasick.append("大学");
            ahoCorasick.append("大学老师");
            ahoCorasick.append("大学毕业");
            ahoCorasick.append("赏识");
            ahoCorasick.append("阿谀奉承");
            ahoCorasick.append("孝顺");
            ahoCorasick.append("开始");
        });
    }

    /**
     * 新增目标匹配字段
     * @param word 将要被匹配到单词
     */
    public void addAhoMatchingData(String word){
        this.executors.execute((() ->{
           R.getConfig().getAhoCorasick().append(word);
        }));
    }

    /**
     * 删除目标匹配字段
     */
    public void removeMatchingData(String word){
        this.executors.execute(() ->{
            R.getConfig().getAhoCorasick().remove(word);
        });
    }

    /**
     * 找到文本区域中的匹配区间
     * 主要是更新用于受：对象的关键字分析
     * 和各阶段的
     * 这里字符串的长度经过每个循环都要
     * 自增1，因\n，作为存在的字符而做分隔符
     *
     */
    public void findMatchingSpans(String text){
        String[] paras = text.split("\n");
        this.answers.clear();
        this.asks.clear();
        //文本的长度
        int length = 0;
        int lastLen = 0;
        //记录结构文本区间
        Pair<Integer, IndexRange> pair = null;
        IndexRange range = null;
        //是否要开始记录的变量
        boolean isBegin = false;
        //记录上次记录记录的对象: true为受:、false为访、null为没有相关记录不做插入:
        Boolean hasObj = null;
        int lastBeginCount = 0;
        for (int count = 0; count < paras.length; count++){
            String curr = paras[count];
            int currLen = curr.length();
            if (curr.startsWith("访：")
                    || curr.startsWith("访:")){
                //System.out.println(curr);
                if (!isBegin)
                    isBegin = true;
                //记录受：
                else {
                    range = new IndexRange(lastLen, length);
                    //System.out.println(text.substring(lastLen, length) + " :end");
                    pair = new Pair<>(lastBeginCount, range);
                    this.answers.add(pair);
                }
                lastLen = length;
                lastBeginCount = count;
                hasObj = false;
            }
            else if (curr.startsWith("受：")
                    || curr.startsWith("受:")){
                //System.out.println(curr);
                if (!isBegin)
                    isBegin = true;
                //记录访：
                else {
                    range = new IndexRange(lastLen, length);
                    //System.out.println(text.substring(lastLen, length) + " :end");
                    pair = new Pair<>(lastBeginCount, range);
                    this.asks.add(pair);
                }
                lastLen = length;
                lastBeginCount = count;
                hasObj = true;
            }
            length += currLen;
            //当处于最后一行时：
            if (count + 1 == paras.length){
                range = new IndexRange(lastLen, length);
                pair = new Pair<>(lastBeginCount, range);
                if (hasObj != null && hasObj) this.answers.add(pair);
                else if (hasObj != null && !hasObj) this.asks.add(pair);
            }
            length ++;
        }
    }
    /**
     * 进行单词区分的方法
     * 用在ac自动机的方法调用后
     */
    public void distinctWords(){

    }
    /**
     * 对单词进行相应的归纳并使得其能够高亮
     * @return
     */
    private boolean shouldEnroll(String word, int start, int end, String text){
        //清空历史匹配记录
        this.words.clear();
        for (Pair<Integer, IndexRange> pair: this.answers){
            IndexRange range = pair.getValue();
            if (range.getStart() <= start && end < range.getEnd()){
                //添加单词到匹配序列当中
                this.words.add(text);
                return true;
            }
        }
        return false;
    }

    /**
     * ac自动机匹配目标字段
     * 根据相应的归类进行字段的高亮
     * @param text
     * @return
     */
    private StyleSpans<Collection<String>> getStyleSpansWithoutRE(String text){
        AhoCorasick corasick = R.getConfig().getAhoCorasick();
        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
        IntegerProperty init = new SimpleIntegerProperty(0);
        corasick.find(text, ((word, start, end) -> {
            StyleSpan<Collection<String>> styleSpan = null;
            if (!this.shouldEnroll(word, start, end, text))
                return;
            builder.add(Collections.EMPTY_LIST, start - init.get());
            init.set(end);
            styleSpan = new StyleSpan<>(Collections.singleton("keyword"), end - start);
            builder.add(styleSpan);
        }));
        builder.add(Collections.EMPTY_LIST, text.length() - init.get());

        return builder.create();
    }
    /**
     * 正则表达式匹配特殊含义字符含义
     * 参考转录代号说明1-8
     * @param text
     * @return
     */
    public StyleSpans<Collection<String>> getStyleSpansWithRE(String text){

        int length = text.length();
        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
        Matcher matcher = PATTERN.matcher(text);
        int start = 0;
        while (matcher.find()){
            int tmp = matcher.start();
            builder.add(Collections.emptyList(), tmp - start);
            String styleClass =
                    matcher.group(1) != null? "parentheses":
                    matcher.group(2) != null? "parentheses":
                    matcher.group(3) != null? "answer-title": null;
            //当styleClass 为空时程序将自动跳出
            assert styleClass!= null;
            builder.add(Collections.singleton(styleClass), matcher.end() -tmp);
            start = matcher.end();
        }
        if (start != length )
            builder.add(Collections.emptyList(), length - start);
        StyleSpans<Collection<String>> theNew = builder.create();
        return theNew;
    }


    /**
     * 获取用于文本渲染的styleSpans<Collection<String>>的任务
     * @param text
     * @return
     */
    public Task<StyleSpans<Collection<String>>> getStyleSpansTask(String text){
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                findMatchingSpans(text);
                StyleSpans<Collection<String>> theNew = getStyleSpansWithoutRE(text);
                StyleSpans<Collection<String>> theOld = getStyleSpansWithRE(text);
                theNew = theNew.overlay(theOld, (first, next) -> {
                    Collection<String> collection = new ArrayList<>();
                    collection.addAll(first);
                    collection.addAll(next);
                    return collection;
                });
                return theNew;
            }
        };
        this.executors.execute(task);
        return task;
    }

    /**
     * 在回车后根据索引返回特定的字符串
     * 当索引为奇数时将返回"\n访：",
     * 当索引为偶数时将返回"\n受："
     * @param index
     * @return
     */
    public String getStageChangingTextForIndex(int index){
        if (index % 2 == 1)
            return "\n访：\t";
        return "\n受：\t";
    }

    /**
     * 文本区文本重整,将每行的字数限制为30个字
     * 期间构建访、受所在的段落序号(构建前清空)
     * @param text 各个段落字符串
     * @param restrictParaLength 段落限制的字符串
     * @return 返回该文本区文本重整任务
     */
    public Task<String> textReformed(String text, int restrictParaLength){
        Task task =  new Task<String>() {

            protected String call() throws Exception {
                StringBuilder stringBuilder = new StringBuilder();
                //去除特定条件下的空格和tab空格键和回车键
                int firstIndex = text.indexOf("访：");
                if (firstIndex == -1)
                    firstIndex = text.indexOf("访:");
                firstIndex = firstIndex == -1? 0: firstIndex;
                stringBuilder.append(text.substring(0, firstIndex));
                char[] chars = text.toCharArray();
                for (int count = firstIndex; count < chars.length; count++){
                    if (chars[count] == ' ' || chars[count] == '\t' || chars[count] == '\n')
                        continue;
                    stringBuilder.append(chars[count]);
                }
                //stringBuilder.append("\n");
                //遇到访：或者受：将自动进行换行
//                Pattern pattern = Pattern.compile(BEGIN_PATTERN, Pattern.CASE_INSENSITIVE);
//                Matcher matcher = pattern.matcher(stringBuilder);
//                int start = 0;//记录段落序号开始和结束
//                while (matcher.find()){
//                    String matchStr = matcher.group();
//                    if (matchStr == null)
//                        continue;
//                    start = matcher.start();
//                    if (matcher.start() != 0 && stringBuilder.charAt(start - 1) != '\n') {
//                        stringBuilder.insert(start, "\n");
//                        stringBuilder.insert(matcher.end() + 1, '\t');
//                    }
//                    else
//                        stringBuilder.insert(matcher.end(), '\t');
//                }
                IntegerProperty offset = new SimpleIntegerProperty(0);
                customMather.find(stringBuilder.toString(), ((word, start, end) -> {
                    int set = offset.get();
                    start += set;
                    end += set;
                    if (start != 0 && stringBuilder.charAt(start - 1) != '\n') {
                        stringBuilder.insert(start, "\n");
                        stringBuilder.insert(end + 1, '\t');
                        offset.set(set + 2);
                    }
                    else {
                        stringBuilder.insert(end.intValue(), '\t');
                        offset.set(set + 1);
                    }
                }));

                //给每行限定字数,每行的字数为30个字
                Pattern pattern = Pattern.compile("[访受][:：].*");
                Matcher matcher = pattern.matcher(stringBuilder);
                while (matcher.find()){
                    String target = matcher.group();
                    int base = matcher.start();
                    for (int count = 0; count < target.length(); count++){
                        if ((count + 1) % restrictParaLength == 0) {
                            stringBuilder.insert(base + count, "\n\t");
                            base += 2;
                        }
                    }
                }
                return stringBuilder.toString();
            }
        };
        return task;
    }

}