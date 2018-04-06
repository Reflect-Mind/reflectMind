package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.VocabDao;
import cn.edu.lingnan.dao.impl.VocabDaoImpl;
import cn.edu.lingnan.pojo.*;
import cn.edu.lingnan.sdk.algorithms.wordLearning.WordLearning;
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

    //查找Vocab表中的所有记录
    @Override
    public List<Vocab> findAllRecord() {
        return vocabDao.getListByHQL("from Vocab");
    }


    //key: 词频查询功能方法
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


    //key: 词汇统计功能方法
    @Override
    public List<PsychoTree> getPsychoTreeByContent(List<String> content) {

        List<PsychoTree> psychoTree;
        List<String> cateList;

        //分类查询
        //  获取分类
        cateList = getCategoryByContent( content );
        //  统计分类频数
        psychoTree = cateAppearCount( cateList );
        //  频率计算
        psychoTree = frqCalculator( psychoTree );

        //主题查询
        psychoTree = themeQuery( psychoTree );

        return psychoTree;
    }

    //key: 新词预测功能方法
    @Override
    public List<Vocab> getNewWordByText(String str) {

        List<Vocab> coverVocab = null;
        List<Vocab> oldVocab = null;
        List<Vocab> newVocab = null;
        WordLearning wordLearner = new WordLearning();

        long start = System.currentTimeMillis();
        coverVocab = wordLearner.learnWordFromText( str );
        long end = System.currentTimeMillis();

        System.out.println("扫描新词用时：" + ( end - start ) + "ms" );

        oldVocab = findAllRecord();

        newVocab = getNewVocab( coverVocab, oldVocab );

        return newVocab;
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


    //统计分类频数
    private List<PsychoTree> cateAppearCount( List<String> cateList ) {

        List<PsychoTree> psychoTree = new ArrayList<>();

        String cate;
        boolean repeat;
        int i, j, temp;

        //分类频数统计
        for ( i=0; i<cateList.size(); i++ ) {
            //cate是cateList表中的某一个单词
            cate = cateList.get(i);

            //检查psychoTree中是否存在cate
            repeat = false;
            for( j=0; j<psychoTree.size(); j++ )

                if( psychoTree.get(j).getCategory().equals( cate ) ) {
                    //如果cate存在于psychoTree中，更新psychoTree中对应字符的频数
                    temp = psychoTree.get(j).getAppearnum();
                    psychoTree.get(j).setAppearnum( temp + 1 );

                    //flag=1表示字符tc存在于clist中
                    repeat = true;
                }
            if( repeat == false ) {
                //如果不存在于psychoTree中，在psychoTree中增加cate
                PsychoTree vc = new PsychoTree();
                vc.setCategory( cate );
                vc.setAppearnum( 1 );
                psychoTree.add( vc );
            }
        }

        return psychoTree;
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


    //通过单词内容获取分类
    private List<String> getCategoryByContent( List<String> content ) {

        List<Integer> categoryIdList = new ArrayList<Integer>();
        List<String> cateList = new ArrayList<>();
        int categoryId, id1, id2;
        String word1, word2, category;

        //  1.获取数据库中的vocab表, getCategoryIdByContent
        List<Vocab> vocab = R.getConfig().getVocabList();

        for( int i=0; i<content.size(); i++ ) {
            word1 = content.get(i);

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
                    cateList.add( category );
                }
            }
        }

        return cateList;
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


    //分类频率的计算（某单词频率 = 某单词频数 / 所有单词频数的和）
    private  List<PsychoTree> frqCalculator( List<PsychoTree> psychoTree ) {

        int sum;
        double ind, frq;

        //计算总频数
        sum = 0;
        for ( PsychoTree p : psychoTree ) {
            sum = sum + p.getAppearnum();
        }

        //计算频率
        for (int i = 0; i < psychoTree.size(); i++) {
            ind = psychoTree.get(i).getAppearnum();
            frq = ind / sum;
            psychoTree.get(i).setFrq( frq );
        }

        return psychoTree;
    }


    //主题查询
    private List<PsychoTree> themeQuery( List<PsychoTree> psychoTree ) {

        List<Integer> themeIdList = new ArrayList<Integer>();
        int themeId, id1, id2;
        String cate1, cate2, theme;

        //  1.获取数据库中的category表, getThemeIdByContent
        CategoryService categoryService = new CategoryServiceImpl();
        List<Category> categoryList = categoryService.findAllRecord();

        for( int i=0; i<psychoTree.size(); i++ ) {
            cate1 = psychoTree.get(i).getCategory();

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
                    psychoTree.get(i).setTheme( theme );
                }
            }
        }

        return psychoTree;
    }


    //新词查询
    private  List<Vocab> getNewVocab( List<Vocab> coverVocab, List<Vocab> oldVocab ) {

        List<Vocab> newVocab = new ArrayList<>();
        boolean repeat = false;
        String word1, word2;
        int sum;
        double ind, frq;

        //将两个词汇列表对比，得出新词列表
        for ( int i=0; i<coverVocab.size(); i++ ) {
            repeat = false;
            word1 = coverVocab.get(i).getContent();

            for ( int j=0; j<oldVocab.size(); j++ ) {
                word2 = oldVocab.get(j).getContent();
                if( word1.equals( word2 ) )
                    repeat = true;
            }

            if ( repeat == false ) {
                Vocab voc = new Vocab();
                voc.setContent( coverVocab.get(i).getContent() );
                voc.setAppearnum( coverVocab.get(i).getAppearnum() );
                voc.setWordlen( coverVocab.get(i).getWordlen() );
                voc.setSolid( coverVocab.get(i).getSolid() );
                voc.setEntropy( coverVocab.get(i).getEntropy() );
                newVocab.add( voc );
            }
        }

        //计算总频数
        sum = 0;
        for ( Vocab v : newVocab ) {
            sum = sum + v.getAppearnum();
        }

        //计算频率
        for ( int i = 0; i < newVocab.size(); i++ ) {
            ind = newVocab.get(i).getAppearnum();
            frq = ind / sum;
            newVocab.get(i).setFrq( frq );
        }

        return  newVocab;
    }

}