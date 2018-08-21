package cn.edu.lingnan.sdk.algorithms.ahoCorasick;

import cn.edu.lingnan.pojo.Vocab;

import java.util.List;

/**
 * Created by Administrator on 2018/2/14.
 */
public interface AhoCorasick {

    void append(List<Vocab> list);
    void append(String[] words);
    void append(Vocab vocab);
    void append(String word);
    void remove(String word);
    void remove(List<? extends String> words);
    void remove(String[] words);
    void removeAll();
    void find(String text, MatchListener matchListener);

}
