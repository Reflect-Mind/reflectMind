package cn.edu.lingnan.service;

import cn.edu.lingnan.pojo.FrqTree;
import cn.edu.lingnan.pojo.Vocab;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 * Last Edited by Mechan on 2018/3/30.
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
    public List<FrqTree> getFrqTreeByContent(List<String> content );

    //查询数据库Vocab表的所有记录
    public List<Vocab> findAllRecord();

}
