package cn.edu.lingnan.service.command;

import cn.edu.lingnan.pojo.FrqTree;
import cn.edu.lingnan.pojo.PsychoTree;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import cn.edu.lingnan.utils.R;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * Last edited by Mechan on 2018/3/30.
 * Modified method getVocabTask's name.
 */
public class TextWorkspaceRightCommand extends AbstractCommand {

    private VocabService vocabService = new VocabServiceImpl();

    /**
     *
     * @return
     */
    public Task<List<Vocab>> getVocabTask (){

        Task<List<Vocab>> task = new Task<List<Vocab>>() {

            @Override
            protected List<Vocab> call() throws Exception {
                List<Vocab> vocabs = null;
                try {
                    vocabs = vocabService.findAllPsyChoVocab();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return vocabs;
            }
        };
        return task;
    }


    /**
     * 返回用于词频查询的FrqTable
     * @return List<FrqTree>
     */
    public  List<FrqTree> getFrqTree() {

        List<FrqTree> voc = null;

        //注意content列表可能为空
        List<String> content = R.getConfig().getWords();
        voc = vocabService.getFrqTreeByContent( content );

        return voc;
    }


    /**
     * 返回用于词汇统计的PsychoTree
     * @return List<PsychoTree>
     */
    public  List<PsychoTree> getPsychoTree() {

        List<PsychoTree> voc = null;

        //注意content列表可能为空
        List<String> content = R.getConfig().getWords();
        voc = vocabService.getPsychoTreeByContent( content );

        return voc;
    }


    /**
     * 返回用于新词预测的PsychoTree
     * @return List<PsychoTree>
     */
    public  List<Vocab> getNewWordTree() {

        List<Vocab> voc = null;

        //注意str可能为空
        String str = R.getConfig().getTextProperty();

        System.out.println("输出受访者对白");
        System.out.println( str );

//        voc = vocabService.getPsychoTreeByContent( str );

        return null;
    }

}
