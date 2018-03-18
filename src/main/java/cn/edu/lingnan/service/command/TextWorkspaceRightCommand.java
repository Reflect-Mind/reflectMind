package cn.edu.lingnan.service.command;

import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import cn.edu.lingnan.utils.R;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class TextWorkspaceRightCommand extends AbstractCommand {

    private VocabService vocabService = new VocabServiceImpl();

    /**
     *
     * @return
     */
    public Task<List<Vocab>> getVacabTask (){

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
     * 返回一个可以显示在界面上的WordTable
     * @return List<Vocab>
     */
    public  List<Vocab> getWordTable() {

        List<Vocab> voc = null;

        List<String> content = R.getConfig().getWords();
        voc = vocabService.getByContent( content );

//        System.out.println("原始单词：");
//        for ( int i=0; i<content.size(); i++ ) {
//            System.out.println( content.get(i) );
//        }
//        System.out.println("\n");


        //控制台输出
        System.out.println("已识别单词的数目：" + voc.size() );
        for( int i=0; i<voc.size(); i++ ) {
            System.out.println( voc.get(i).getContent() + "\t" +
                    voc.get(i).getAppearnum());
        }

        return voc;

    }
}
