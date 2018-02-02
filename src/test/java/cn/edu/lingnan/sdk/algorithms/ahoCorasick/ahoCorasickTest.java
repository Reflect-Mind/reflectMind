package cn.edu.lingnan.sdk.algorithms.ahoCorasick;

import cn.edu.lingnan.sdk.algorithms.ahoCorasick.AhoCorasick;
import cn.edu.lingnan.sdk.algorithms.ahoCorasick.MatchListener;
import org.junit.Test;

/**
 * Created by Administrator on 2018/2/1.
 */
public class ahoCorasickTest {
    @Test
    public void test() {
        String[] words = new String[]{
                "商品",
                "和服",
                "服务器",
                "服务"
        };
        AhoCorasick ahoCorasick = new AhoCorasick();
        ahoCorasick.append(words);
        ahoCorasick.append("服务端");
        ahoCorasick.find("商品和服务端", new MatchListener() {
            @Override
            public void match(String word, Integer start, Integer end) {
                System.out.println(word);
            }
        });
    }

}
