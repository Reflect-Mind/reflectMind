package cn.edu.lingnan.sdk.algorithms.wordLearning;

import cn.edu.lingnan.pojo.Vocab;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CharCalc {

    //读取文件到str1
    public String readTextFile( File file ) throws IOException;

    //对原文text进行预处理，以去掉非汉字字符
    public String preProcessText( String text );

    //将文本text中的不同字符存储到clist列表并统计频数
    public List<Vocab> readChar( String text, List<Vocab> cs) throws IOException;

    //对词汇列表按某种规则排序
    public List<Vocab> sortVocab( List<Vocab> clist );

    //检索单词是否已经存在于词汇表中
    public boolean wordExist( String str2, List<Vocab> vlist );

    //分析str2是否构成一个单词
    public boolean wordAnalyze( String str1, String str2, List<Vocab> clist, List<Vocab> vlist, int minAppear);

    //确定单词str2中某字符的频数
    public int findChar( String str2, int loc, List<Vocab> clist );

    //确定单词str2中某单词的出现次数
    public int findWord( String str2, int loc, List<Vocab> vlist, int plus );

    //计算信息熵
    public void entropfy( String strX, List<Vocab> vlist );

    //对不同字符进行频数计算
    public int countChar( char ch, List<Vocab> en );

    //计算邻字信息熵
    public double countEntropy( List<Vocab> en );

    //通过信息熵过滤单词
    public int filterByEntropy( List<Vocab> vlist );

    //用原文检查单词
    public int textCheck( String text, List<Vocab> vlist );

    //对vlist的其它列进行初始化
    public int initColumn( String text, List<Vocab> vlist );

}
