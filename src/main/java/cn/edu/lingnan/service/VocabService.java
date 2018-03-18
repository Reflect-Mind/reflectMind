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


    /**
     * 输入一个Vocab.content列表，返回一个Vocab列表
     * 在getByContent中完成词频统计、分类查询、主题查询
     * @param content
     * @return List<Vocab>
     */
    public List<Vocab> getByContent( List<String> content );

}
