package cn.edu.lingnan.command;

import cn.edu.lingnan.pojo.CatchingWord;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.sdk.algorithms.ahoCorasick.AhoCorasick;
import cn.edu.lingnan.sdk.algorithms.ahoCorasick.AhoCorasickImpl;
import cn.edu.lingnan.sdk.plain.IndexRange;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.util.Pair;
import org.fxmisc.richtext.StyleClassedTextArea;
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
    private List<CatchingWord> catchingWords = R.getConfig().getCatchingWords();

    //单独线程执行者
    private ExecutorService executors = Executors.newSingleThreadExecutor();
    //获取心理词汇的服务类
    private VocabService vocabService = new VocabServiceImpl();

    private Config config = R.getConfig();

    //是否标记词汇属性
    private BooleanProperty markVocabs = config.markVocabsProperty();

    //自定义ac自动机：用于关键词的搜索
    private AhoCorasick customAhoCorasick = new AhoCorasickImpl();

    //上一个给标记的词汇属性
    private IntegerProperty searchingWordIndexProperty = config.searchingWordIndexProperty();

    //本次被搜索的单词的总数
    private IntegerProperty searchingWordCountProperty = config.searchingWordCountProperty();

    //设置文本域:分层不理想，建议少用
    private StyleClassedTextArea codeArea = null;
    public void setCodeArea(StyleClassedTextArea codeArea) {
        this.codeArea = codeArea;
    }

    /**
     * 更新目标的搜索词汇
     * @param isAdd 将要在ac自动机中删除的字符串
     * @param word 将要被添加和删除的词汇
     */
    public void updateSearchWord(boolean isAdd, List<? extends String> word){
        if (isAdd)
            word.forEach(this.customAhoCorasick::append);
        else
            this.customAhoCorasick.remove(word);
    }

    /**
     * 判别当前选择到的词汇
     *
     * @param selectionText
     * @return  返回true表示当中过滤词汇满足条件， 否则就不满足条件
     */
    public boolean validateSelectionText(String selectionText){
        if (selectionText.length() == 0)
            return false;
        return true;
    }

    /**
     * 更新目标匹配字段:
     * 将数据库中的心理词汇加载到ac自动机中，以进行心理
     * 词汇的匹配分析
     */
    public void updateAhoMatchingData(){
        executors.execute(() -> {
            //清空心理词汇信息
            ObservableList<Vocab> vocabList = R.getConfig().getVocabList();
            vocabList.clear();
            AhoCorasick ahoCorasick = R.getConfig().getAhoCorasick();

            //重新填充vocab信息词汇
            vocabList.addAll(this.vocabService.findAllPsyChoVocab());
            //同时填充之ac自动机当中
            ahoCorasick.append(vocabList);
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
     * 对单词进行相应的归纳并使得其能够高亮
     * @return
     */
    private boolean shouldEnroll(String word, int start, int end, String text){
        for (Pair<Integer, IndexRange> pair: this.answers){
            IndexRange range = pair.getValue();
            if (range.getStart() <= start && end < range.getEnd()){
                //添加单词到匹配序列当中:将大致的索引范围放入实体类当中 => 省内存
                this.catchingWords.add(new CatchingWord(word, range));
                return true;
            }
        }
        return false;
    }


    /**
     * 扫描文本字符串获取目标搜索关键词的样式区间
     * @param text 文本字符串
     */
    private StyleSpans<Collection<String>>  getStyleSpansByAhoCorasickOnSearchingWord(String text){
        StyleSpansBuilder<Collection<String>> builder = new StyleSpansBuilder<>();
        int init = 0;
        //记录当前段落关键词的所在段落和在原文中出现的范围
        List<Pair<Integer, IndexRange>> indexRanges = new ArrayList<>();
        int length = text.length();
        this.customAhoCorasick.find(text, ((word, start, end, para) ->
          indexRanges.add(new Pair<>(para, new IndexRange(start, end)))));
        //设置当前已经匹配到的字符串数目
        this.searchingWordCountProperty.set(indexRanges.size());
        //构建styleSpans样式
        for (int count = 0; count < indexRanges.size(); count++){
            Pair<Integer, IndexRange> pair = indexRanges.get(count);
            IndexRange range = pair.getValue();
            builder.add(Collections.emptyList(), range.getStart() - init);
            if (this.searchingWordIndexProperty.get() == count) {
                builder.add(Collections.singleton("currently-searching-word"), range.getLength());
                //更新到当前段落(触犯禁忌)
                this.codeArea.showParagraphInViewport(pair.getKey());

            }
            else
                builder.add(Collections.singleton("searching-word"), range.getLength());
            init = range.getEnd();
        }
        if (init != length)
            builder.add(Collections.emptyList(), length  - init);
        return builder.create();
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
        //清空历史匹配记录
        this.catchingWords.clear();

        corasick.find(text, ((word, start, end, para) -> {
            StyleSpan<Collection<String>> styleSpan = null;

            if (!this.shouldEnroll(word, start, end, text))
                return;
            if (!this.markVocabs.get())
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
     * @param text 将要被样式化文本字符串
     * @param findMatchingSpan 是否更新访、受区间
     * @return
     */
    public Task<StyleSpans<Collection<String>>> getStyleSpansTask(String text, boolean findMatchingSpan){
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                if (findMatchingSpan)
                    findMatchingSpans(text);
                StyleSpans<Collection<String>> theNew = getStyleSpansWithoutRE(text);
                StyleSpans<Collection<String>> theOld = getStyleSpansWithRE(text);
                theNew = theNew.overlay(theOld, (first, next) -> {
                    Collection<String> collection = new ArrayList<>();
                    collection.addAll(first);
                    collection.addAll(next);
                    return collection;
                });
                theNew = theNew.overlay(getStyleSpansByAhoCorasickOnSearchingWord(text), (first, next) -> {
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
                firstIndex = firstIndex == -1? text.length(): firstIndex;
                stringBuilder.append(text.substring(0, firstIndex));
                char[] chars = text.toCharArray();

                for (int count = firstIndex; count < chars.length; count++){
                    if (chars[count] == ' ' || chars[count] == '\t' || chars[count] == '\n')
                        continue;
                    stringBuilder.append(chars[count]);
                }
                IntegerProperty offset = new SimpleIntegerProperty(0);
                customMather.find(stringBuilder.toString(), ((word, start, end, para) -> {
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
