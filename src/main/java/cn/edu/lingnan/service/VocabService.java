package cn.edu.lingnan.service;

import cn.edu.lingnan.pojo.FrqTree;
import cn.edu.lingnan.pojo.PsychoTree;
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


    //查询数据库Vocab表的所有记录
    public List<Vocab> findAllRecord();


    /**
     * 在getByContent中完成词频查询
     * @param content
     * @return List<FrqTree>
     */
    public List<FrqTree> getFrqTreeByContent( List<String> content );


    /**
     * 在getByContent中完成词汇统计
     * @param content
     * @return List<PsychoTree>
     */
    public List<PsychoTree> getPsychoTreeByContent( List<String> content );


    /**
     * 新词预测
     * @param str
     * @return List<Vocab>
     */
    public List<Vocab> getNewWordByText( String str );
}
