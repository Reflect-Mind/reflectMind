package cn.edu.lingnan.sdk.algorithms.wordLearning;

import cn.edu.lingnan.pojo.Vocab;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/2.
 */
public class learningTest {

    @Test
    public void testWordLearning(){

        String text;
        CharCalc calc = new CharCalcImpl();
        WordLearning wordLearn = new WordLearning();
        List<Vocab> vlist = new ArrayList<>();
        File file = new File( "./src/main/resources/text/testMarterial" );

        try {
            text = calc.readTextFile( file );
            System.out.println( "文本长度: " + text.length() );

            vlist = wordLearn.learnWordFromText( text );

            System.out.println( "单词列表长度: " + vlist.size() );
            System.out.println( "单词列表" );
            System.out.println();
            System.out.println( "内容\t频数\t凝固值\t信息熵\t频率\t词长" );
            System.out.println();

            for ( int i=0; i<vlist.size(); i++ ) {
                System.out.println( vlist.get(i).getContent() + "\t" +
                        vlist.get(i).getAppearnum() + "\t" +
                        vlist.get(i).getSolid() + "\t" +
                        vlist.get(i).getEntropy() + "\t" +
                        vlist.get(i).getFrq() + "\t" +
                        vlist.get(i).getWordlen() );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
