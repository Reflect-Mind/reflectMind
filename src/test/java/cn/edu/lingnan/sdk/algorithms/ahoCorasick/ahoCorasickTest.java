package cn.edu.lingnan.sdk.algorithms.ahoCorasick;

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
                "服务"
        };
        AhoCorasick ahoCorasick = new AhoCorasickImpl();
        ahoCorasick.append(words);
        ahoCorasick.append("服务端");
        ahoCorasick.remove("和服");
        ahoCorasick.find("商品和服务端", new MatchListener() {
            @Override
            public void match(String word, Integer start, Integer end) {
                System.out.println(word);
            }
        });
    }

}
