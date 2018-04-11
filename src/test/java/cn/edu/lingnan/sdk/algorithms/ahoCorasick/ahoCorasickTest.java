package cn.edu.lingnan.sdk.algorithms.ahoCorasick;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Administrator on 2018/2/1.
 */
public class ahoCorasickTest {
    @Test
    public void test() {

        String[] words = new String[]{
          "访：",
          "受：",
          "\n"
        };
        AhoCorasick ahoCorasick = new AhoCorasickImpl();
        ahoCorasick.append(words);
        ahoCorasick.append("访：");
        ahoCorasick.append("受：");
        ahoCorasick.append("访:");
        ahoCorasick.append("访：");
        ahoCorasick.remove(words);
        ahoCorasick.append(words);
        ahoCorasick.find("受：你来受受这个世界上，啊，你你的人生轨迹，或许，我很相信这个话，你，你冥冥之中上天早已为你安排，既然它安排我是这么走的，我，我也去走了，可能当时你遇到困难的时候，你真的会觉得，哎呀，我可能快挺不过去了，而事实上你咬一咬牙挺过去了，你再回头去看，你发现，哎呀，也不过如此，所以我觉得我对我人生这个经历怎么看待，我没有什么很大的感慨，或许（停顿2秒）别人跟你也是一样的，啊，就这样\n" +
                "访：嗯，好，那我们就结束\n" +
                "受：可以了是吧\n" +
                "访：可以了", new MatchListener() {
            @Override
            public void match(String word, Integer start, Integer end, Integer para) {
                System.out.println(word + ": " + para);
            }
        });

    }

}
