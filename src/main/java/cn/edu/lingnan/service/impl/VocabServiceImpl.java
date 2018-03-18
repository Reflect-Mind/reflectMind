package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.VocabDao;
import cn.edu.lingnan.dao.impl.VocabDaoImpl;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.service.VocabService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 *  Edited by Mechan on 2018/3/18.
 */
public class VocabServiceImpl implements VocabService {

    private VocabDao vocabDao = new VocabDaoImpl();

    @Override
    public List<Vocab> findAllPsyChoVocab() {
        String hqlString  = "from Vocab order order by content";
        List<Vocab> vocabList = this.vocabDao.getListByHQL(hqlString);
        return vocabList;
    }


    @Override
    public List<Vocab> getByContent(List<String> content) {

        List<Vocab> wordTable = new ArrayList<Vocab>();
        String word;
        boolean repeat;
        int i, j, temp;


        //词频统计
        for ( i=0; i<content.size(); i++ ) {

            //word是content表中的某一个单词
            word = content.get(i);

            //检查wordTable中是否存在word
            repeat = false;
            for( j=0; j<wordTable.size(); j++ )

                if( wordTable.get(j).getContent().equals( word ) ) {
                    //如果word存在于wordTable中，更新wordTable中对应字符的频数(只在内存中，不存入磁盘）
                    temp = wordTable.get(j).getAppearnum();
                    wordTable.get(j).setAppearnum( temp + 1 );

                    //flag=1表示字符tc存在于clist中
                    repeat = true;
                }
            if( repeat == false ) {
                //如果word不存在于wordTable中，在wordTable中增加word
                Vocab vc = new Vocab();
                vc.setContent( word );
                vc.setAppearnum( 1 );
                wordTable.add( vc );
            }
        }

        //分类查询

        //主题查询

        return wordTable;
    }

}