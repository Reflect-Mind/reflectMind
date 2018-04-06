package cn.edu.lingnan.service.command;

import cn.edu.lingnan.pojo.FrqTree;
import cn.edu.lingnan.pojo.PsychoTree;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import cn.edu.lingnan.utils.R;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.IndexRange;
import javafx.util.Pair;

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


    public Task<List<FrqTree>> getFrqTreeTask(){
        return new Task<List<FrqTree>>() {
            @Override
            protected List<FrqTree> call() throws Exception {
                return getFrqTree();
            }
        };
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


    public Task<List<PsychoTree>> getPsychoTreeTask(){
        return new Task<List<PsychoTree>>() {
            @Override
            protected List<PsychoTree> call() throws Exception {
                return getPsychoTree();
            }
        };
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


    public Task<List<Vocab>> getNewWordTreeTask(){
        return new Task<List<Vocab>>() {
            @Override
            protected List<Vocab> call() throws Exception {
                return getNewWordTree();
            }
        };
    }

    /**
     * 返回用于新词预测的PsychoTree
     * @return List<PsychoTree>
     */
    private List<Vocab> getNewWordTree() {

        //新词列表
        List<Vocab> voc = null;

        //str为生命故事文本
        String str = "";
        if( R.getConfig().getTextProperty() != null )
            str = R.getConfig().getTextProperty();

        str = str.replace( "受：\t", "ee");
        str = str.replace( "访：\t", "er");
        str = str.replace( "嗯", "e");
        str = str.replace( "哦", "o");

        voc = vocabService.getNewWordByText( str );

        return voc;
    }

}
