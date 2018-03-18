package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.VocabDao;
import cn.edu.lingnan.dao.impl.VocabDaoImpl;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.service.VocabService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
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

        //词频统计
        //将文本text中的不同字符存储到clist列表并统计频数
//        for ( int i=0; i<content.size(); i++ ) {
//
//            tc = text.charAt(i);
//            ts = String.valueOf(tc);
//
//            //检查clist中是否存在字符tc
//            flag=0;
//            for( j=0; j<clist.size(); j++ )
//                if( clist.get(j).getContent().equals( ts ) ) {
//
//                    //如果字符tc存在于clist中，更新clist中对应字符的频数
//                    temp = clist.get(j).getAppearnum();
//                    clist.get(j).setAppearnum( temp + 1 );
//
//                    //flag=1表示字符tc存在于clist中
//                    flag = 1;
//                }
//            if( flag != 1 ) {
//                //如果字符tc不存在于clist中，在clist中增加tc
//                Vocab vc = new Vocab();
//                vc.setContent( ts );
//                vc.setAppearnum( 1 );
//                clist.add( vc );
//            }
//        }

//        for ( int i=0; i<content.size(); i++ ) {
//
//            //检查content表中的单词是否已经存在于wordTable中
//            repeat = false;
//            for ( int j=0; j<wordTable.size(); j++ ) {
//
//                word = wordTable.get(j).getContent();
//                if ( word.equals( content.get(i) ) ) {
//                    repeat = true;
//                    j = wordTable.size();
//                }
//            }
//
//            //当content表中的单词不在wordTable中
//            if ( repeat == false ) {
//                Vocab voc = new Vocab();
//                voc.setContent( content.get(i) );
//
//            }
//        }

        //分类查询

        //主题查询

        return wordTable;
    }
}
