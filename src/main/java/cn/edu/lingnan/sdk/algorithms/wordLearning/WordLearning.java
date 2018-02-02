package cn.edu.lingnan.sdk.algorithms.wordLearning;

import cn.edu.lingnan.pojo.Vocab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30.
 * 新词学习算法放置的所在的包
 * @see cn.edu.lingnan.sdk.algorithms.wordLearning
 */
public class WordLearning {

    /* -----------------可供外部调控的参数-----------------*/
    public static int freqConst = -1;		//频数阈值，等于-1表示由系统自动确定
    public static double solidConst = 30;	//凝固程度阈值
    public static double entroConst = 0.687;//信息熵阈值
    public static int wordLength = 7;		//单词的最大长度

    //结果排序的依据，1表示按频数，2表示按凝固程度，3表示信息熵
    public static int sortByConst = 1;


    /**
     *
     * @param text
     * @return 一批符合程序判断条件的新词
     */
    public List<Vocab> learnWordFromText(String text){

        //新建字符存储对象 clist（只存汉字字符）
        List<Vocab> clist = new ArrayList<>();

        //新建单词存储对象 vlist（不包含非汉字字符）
        List<Vocab> vlist = new ArrayList<>();

        //新建文字处理对象 calc
        CharCalc calc = new CharCalcImpl();

        //新建新词处理对象
        WordProcess wpc = new WordProcess();


        try {
            // 对原文text进行预处理，以去掉非汉字字符
            String str1 = calc.preProcessText( text );

            /* -----------------字符分析-----------------*/
            //找出str1中所有的字符并统计频数
            calc.readChar( str1, clist );

            //对clist进行按某种规则排序
            calc.sortVocab( clist );

            /* -----------------学习新词-----------------*/
            //学习频数、凝固程度都不小于阈值的单词
            wpc.newWord( str1, clist, vlist );

            //信息熵处理
            calc.entropfy( str1, vlist );	//计算信息熵
            calc.filterByEntropy( vlist );	//信息熵过滤

            //用原文检查单词
            calc.textCheck( text, vlist );

            //对vlist进行按某种规则排序
            calc.sortVocab( vlist );

            //对vlist的其它列进行初始化
            calc.initColumn( text, vlist );

        } catch (IOException e) {
            e.printStackTrace();
        }

        return vlist;
    }


}



