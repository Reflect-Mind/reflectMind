package cn.edu.lingnan.service;

import cn.edu.lingnan.pojo.Vocab;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */
public interface VocabService {

    /**
     * 查找所有的心理词汇
     * @return
     */
    public List<Vocab> findAllPsyChoVocab();
}
