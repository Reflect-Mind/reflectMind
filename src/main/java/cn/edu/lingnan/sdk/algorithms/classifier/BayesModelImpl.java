package cn.edu.lingnan.sdk.algorithms.classifier;

import cn.edu.lingnan.utils.SerializableUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 * 贝叶斯模型
 */
public class BayesModelImpl extends BayesModel{

    /**
     * 记录分类的名称以及统计各分类的概率
     * @param categories
     */
    private void initCategories(File[] categories){
        double count = 0;
        for (int index = 0; index < categories.length; index++){
            File file = categories[index];
            File[] files = file.listFiles(File::isFile);
            //统计分类
            this.categories.add(file.getName());
            count += files.length;
            this.categoryProbability.add(new Double(files.length));
            this.initSentences(files, index);
        }
        //统计各大分类的概率
        for (int i = 0; i < this.categories.size(); i++) {
            Double aDouble = this.categoryProbability.get(i) / count;
            this.categoryProbability.set(i, aDouble);
//            System.out.println(this.categories.get(i) + ": " + aDouble);
        }
    }

    /**
     * 记录句子以及句子所属的分类
     * 获取纯净版的单词集合
     * @param wordsFile
     * @param categoryId 分类所属Id
     */
    private void initSentences(File[] wordsFile, int categoryId){
        for (File file : wordsFile){
            String sentence = this.getSentencesFromFile(file);
            this.sentences.add(sentence);
            this.categoryIdList.add(categoryId);
            this.tokenizer.setTarget(sentence);
            while (this.tokenizer.hasNext()){
                String word = this.tokenizer.next();
                Matcher matcher = ESCAPE_PATTERN.matcher(word);
                if (!matcher.find())
                  this.words.add(word);
            }
        }
    }

    /**
     * 查找单词是否在单词函数中存在
     * 找到时返回该单词在单词列表的
     * 索引
     * @param target 搜索的目标
     * @return 找到时返回相应的序号，否则返回-1
     */
    private int indexOf(String target){
        int index = 0;
        for (String word: this.words){
            if (word.equals(target))
                return index;
            index++;
        }
        return -1;
    }

    /**
     * 根据各分类填充词向量
     * 初始化词向量分母为2
     * @param wLen 词向量长度
     */
    private void manipulateVector(int wLen){
        //用于求取在某种条件下出现某词汇的概率
        int[] vectorsSum = new int[this.categories.size()];
        Arrays.fill(vectorsSum, 2);
        for (int count = 0; count < this.sentences.size(); count++){
            String sentence = this.sentences.get(count);
            //当前句子所属分类
            int categoryId = this.categoryIdList.get(count);
            //在语句当中得到可疑的单词(加了单词滤过)
            this.tokenizer.setTarget(sentence);
            while (this.tokenizer.hasNext()){
                String word = this.tokenizer.next();
                Matcher matcher = ESCAPE_PATTERN.matcher(word);
                if (matcher.find())
                    continue;
                int index = this.indexOf(word);
                //找到元素, 所处的分类的词向量增加1
                this.wordVectors[categoryId][index] += 1;
                vectorsSum[categoryId] += 1;
            }
        }
//        for (int i = 0; i < vectorsSum.length; i++) {
//            System.out.println(i + ": " + vectorsSum[i] + this.categories.get(i));
//        }
        //整理在各个分类下的词袋模型的概率
        for (int i = 0; i < vectorsSum.length; i++) {
            double[] vector = this.wordVectors[i];
            for (int j = 0; j < vector.length; j++)
                vector[j] = Math.log(vector[j] / vectorsSum[i]);
        }
    }
    /**
     * 记录各个分类的词向量
     * 每个词汇的初始化的值为1
     *
     */
    private void initWordVector(){
        int sLen = this.categories.size();
        int wLen = this.words.size();
        //初始化词向量二维数组
        this.wordVectors = new double[sLen][wLen];
        for (double[] vector: this.wordVectors)
            Arrays.fill(vector, 1);
        this.manipulateVector(wLen);
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
        this.initWordVector();
    }

    /**
     * 根据模型进行单词的分类于预测
     * @param target
     */
     String predict(String target) {
         //设置当前的语句的词向量
         this.values = new double[this.categories.size()];
         for (int i = 0; i < values.length; i++) {
             this.values[i] = Math.log(this.categoryProbability.get(i));
         }
         this.tokenizer = new UglyTokenizer();
         this.tokenizer.setTarget(target);
         while (this.tokenizer.hasNext()){
             String word = this.tokenizer.next();
             Matcher matcher = ESCAPE_PATTERN.matcher(word);
             if (matcher.find())
                 continue;
             int index = this.indexOf(word);
             if (index == -1)
                 continue;
             for (int i = 0; i < this.values.length; i++) {
                 this.values[i] += this.wordVectors[i][index];
             }
         }
         //比较P(C|W)之间的值,值最大的极为本次预测出来的值
         int value = 0;
         for (int i = 0; i < this.values.length; i++) {
//             System.out.println(values[i] + ": " + this.categories.get(i));
             if (this.values[value] < this.values[i])
                 value = i;
         }
         return this.categories.get(value);
    }
}
