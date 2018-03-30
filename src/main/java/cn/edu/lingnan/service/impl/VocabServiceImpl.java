package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.VocabDao;
import cn.edu.lingnan.dao.impl.VocabDaoImpl;
import cn.edu.lingnan.pojo.Category;
import cn.edu.lingnan.pojo.FrqTree;
import cn.edu.lingnan.pojo.Theme;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.service.CategoryService;
import cn.edu.lingnan.service.ThemeService;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 * Last Edited by Mechan on 2018/3/30.
 */
public class VocabServiceImpl implements VocabService {

    private VocabDao vocabDao = new VocabDaoImpl();

    @Override
    public List<Vocab> findAllPsyChoVocab() {
        String hqlString  = "from Vocab order order by content";
        List<Vocab> vocabList = this.vocabDao.getListByHQL(hqlString);
        return vocabList;
    }


    //key: 词频查询
    @Override
    public List<FrqTree> getFrqTreeByContent(List<String> content) {

        List<FrqTree> frqTree;

        //词频统计
        frqTree = appearCount( content );

        //分类查询(通过单词内容获取单词分类)
        frqTree = getCategoryByWord( frqTree );

        //主题查询(通过单词分类获取单词主题)
        frqTree = getThemeByCategory( frqTree );

        return frqTree;
    }


    //词频统计
    private List<FrqTree> appearCount( List<String> content ) {

        List<FrqTree> wordTable = new ArrayList<FrqTree>();
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
                FrqTree vc = new FrqTree();
                vc.setContent( word );
                vc.setAppearnum( 1 );
                wordTable.add( vc );
            }
        }

        return wordTable;
    }


    //通过单词内容获取分类
    private List<FrqTree> getCategoryByWord( List<FrqTree> frqTree ) {

        List<Integer> categoryIdList = new ArrayList<Integer>();
        int categoryId, id1, id2;
        String word1, word2, category;

        //  1.获取数据库中的vocab表, getCategoryIdByContent
        List<Vocab> vocab = R.getConfig().getVocabList();

        for( int i=0; i<frqTree.size(); i++ ) {
            word1 = frqTree.get(i).getContent();

            for( int j=0; j<vocab.size(); j++ ) {
                word2 = vocab.get(j).getContent();

                if( word1.equals( word2 )) {
                    categoryId = vocab.get(j).getCategoryId();
                    categoryIdList.add( categoryId );
                }
            }
        }

        //  2.通过categoryId获取category, getCategoryById
        CategoryService categoryService = new CategoryServiceImpl();
        List<Category> categoryList = categoryService.findAllRecord();

        for( int i=0; i<categoryIdList.size(); i++ ) {
            id1 = categoryIdList.get(i);

            for( int j=0; j<categoryList.size(); j++ ) {
                id2 = categoryList.get(j).getId();

                if( id1 == id2 ) {
                    category = categoryList.get(j).getContent();
                    frqTree.get(i).setCategory( category );
                }
            }
        }

        return frqTree;
    }


    //通过单词分类获取单词主题
    private List<FrqTree> getThemeByCategory( List<FrqTree> frqTree ) {

        List<Integer> themeIdList = new ArrayList<Integer>();
        int themeId, id1, id2;
        String cate1, cate2, theme;

        //  1.获取数据库中的category表, getThemeIdByContent
        CategoryService categoryService = new CategoryServiceImpl();
        List<Category> categoryList = categoryService.findAllRecord();

        for( int i=0; i<frqTree.size(); i++ ) {
            cate1 = frqTree.get(i).getCategory();

            for( int j=0; j<categoryList.size(); j++ ) {
                cate2 = categoryList.get(j).getContent();

                if( cate1.equals( cate2 )) {
                    themeId = categoryList.get(j).getThemeId();
                    themeIdList.add( themeId );
                }
            }
        }

        //  2.通过themeId获取theme, getThemeById
        ThemeService themeService = new ThemeServiceImpl();
        List<Theme> themeList = themeService.findAllRecord();

        for( int i=0; i<themeIdList.size(); i++ ) {
            id1 = themeIdList.get(i);

            for( int j=0; j<themeList.size(); j++ ) {
                id2 = themeList.get(j).getId();

                if( id1 == id2 ) {
                    theme = themeList.get(j).getContent();
                    frqTree.get(i).setTheme( theme );
                }
            }
        }

        return frqTree;
    }


    //查找Vocab表中的所有记录
    @Override
    public List<Vocab> findAllRecord() {
        return vocabDao.getListByHQL("from Vocab");
    }

}