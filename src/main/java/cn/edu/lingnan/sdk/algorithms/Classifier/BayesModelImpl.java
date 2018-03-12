package cn.edu.lingnan.sdk.algorithms.Classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 * 贝叶斯模型
 */
public class BayesModelImpl implements BayesModel{

    /**
     * 分类名字属性
     *
     */
    private List<String> categories = new ArrayList<>();
    /**
     * 分类概率属性
     */
    private List<Double> categoryProbability = new ArrayList<>();

    /**
     * 词概率映射属性
     */
    private List<Map<String, Integer>> wordProbability = new ArrayList<>();

    /**
     * 粗糙分词器
     */
    private Tokenizer tokenizer = new UglyTokenizer();


    private void initCategories(File[] categories){
        double count = 0;
        for (File file: categories){
            File[] files = file.listFiles(File::isFile);
            //统计分类
            this.categories.add(file.getName());
            count += files.length;
            this.categoryProbability.add(new Double(files.length));
            this.initWords(files);
        }

        for (int i = 0; i < this.categories.size(); i++) {
            Double aDouble = this.categoryProbability.get(i) / count;
            this.categoryProbability.set(i, aDouble);
        }
    }

    private void initWords(File[] wordsFile){
        for (File file : wordsFile){
            String sentences = this.getSentencesFromFile(file);
            this.tokenizer.setTarget(sentences);
            while (this.tokenizer.hasNext()){
                String word = this.tokenizer.next();

            }
        }
    }

    /**
     * 单词产生方法
     * @return 给定字符串中
     */
    private String generateWordsFromString(){
        return null;
    }

    /**
     * 返回文本中的字符串
     * @return 文本中的字符串
     */
    private String getSentencesFromFile(File file){
        try {
            Scanner scanner = new Scanner(file);
            String string = scanner.useDelimiter("\\a").hasNext()? scanner.next(): null;
            return string;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void train(File directory) {
        File[] files = directory.listFiles(File::isDirectory);
        this.initCategories(files);
    }

    public static void main(String[] args){
        File file = new File("D://ChnSentiCorp情感分析酒店评论");
        BayesModel model = new BayesModelImpl();
        model.train(file);
    }
}
