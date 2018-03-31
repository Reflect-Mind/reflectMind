package cn.edu.lingnan.service.command;

import cn.edu.lingnan.sdk.Container.PhaseContainer;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.IndexRange;
import javafx.util.Pair;

/**
 * Created by Administrator on 2018/3/30.
 */
public class FillPhaseCommand extends AbstractCommand<Void> {


    /**
     * 获取段落列表
     * @param target 目标文本
     * @param answers 根据文本确定的将要被记录的段落文本（访：）
     * @return
     */
    public ObservableList<String> getStringList(String target
            , ObservableList<Pair<Integer, IndexRange>> answers, PhaseContainer container){
        ObservableList<String> strings = FXCollections.observableArrayList();

        String[] paragraphs = target.split("\n");
        for (Pair<Integer, IndexRange> pair: answers) {
            String paragraph = paragraphs[pair.getKey()];
            if (container.contains(paragraph))
                continue;
            strings.add(pair.getKey() + ": " + paragraph);
        }
        return strings;
    }

    /**
     * 从目标字符串中分离相应的段落索引
     * @param target
     * @return
     */
    public int getParagraphInString(String target){
        int index = target.indexOf(':');
        int para = Integer.valueOf(target.substring(0, index));
        return para;
    }
}
