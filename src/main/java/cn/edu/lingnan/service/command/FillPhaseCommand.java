package cn.edu.lingnan.service.command;

import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.IndexRange;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 * Created by Administrator on 2018/3/30.
 */
public class FillPhaseCommand extends AbstractCommand<Void> {


    private Config config = R.getConfig();

    /**
     * 获取段落列表
     * @param answers 根据文本确定的将要被记录的段落文本（访：）
     * @return
     */
    public ObservableList<Pair<Integer, IndexRange>> getStringList(ObservableList<Pair<Integer, IndexRange>> answers
            , PhaseContainer container){
        ObservableList<Pair<Integer, IndexRange>> strings = FXCollections.observableArrayList();

        for (Pair<Integer, IndexRange> pair: answers) {
            if (container.contains(pair))
                continue;
            strings.add(pair);
        }
        return strings;
    }

    /**
     * 从目标字符串中分离相应的段落索引
     * @param target
     * @return
     */
    public int getParagraphInString(Pair<Integer, IndexRange> target){
        int para = target.getKey();
        return para;
    }

    /**
     * 获取用于设置列表的单元格工厂的callback方法
     * @return
     */
    public Callback<ListView<Pair<Integer, IndexRange>>, ListCell<Pair<Integer, IndexRange>>> getCallback(){

        String[] paragraphs = this.config.textPropertyProperty().get().split("\n");
        return param -> new ListCell<Pair<Integer, IndexRange>>(){
            protected void updateItem(Pair<Integer, IndexRange> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null){
                    this.setText(null);
                    this.setGraphic(null);
                }
                else
                    this.setText(paragraphs[item.getKey()]);
            }
        };
    }
}
