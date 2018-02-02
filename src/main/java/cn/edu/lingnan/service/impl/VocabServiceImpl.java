package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.VocabDao;
import cn.edu.lingnan.dao.impl.VocabDaoImpl;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.service.VocabService;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */
public class VocabServiceImpl implements VocabService {

    private VocabDao vocabDao = new VocabDaoImpl();

    @Override
    public List<Vocab> findAllPsyChoVocab() {
        String hqlString  = "from Vocab order where categoryId is  null order by content";
        List<Vocab> vocabList = this.vocabDao.getListByHQL(hqlString);
        return vocabList;
    }

    public static void main(String[] args){
        VocabService vocabService = new VocabServiceImpl();
        for (Vocab vocab: vocabService.findAllPsyChoVocab()){
            System.out.println(vocab.getContent() + ":" );
        }
    }
}
