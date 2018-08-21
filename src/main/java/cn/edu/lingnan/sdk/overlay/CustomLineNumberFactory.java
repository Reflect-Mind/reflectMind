package cn.edu.lingnan.sdk.overlay;

import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.apache.xmlbeans.impl.xb.ltgfmt.Code;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.reactfx.Change;
import org.reactfx.EventStreams;

import java.time.Duration;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2018/2/21.
 */

/**
 * 自定义行号生成器
 * 使用类选择器line-box作为为被选中的样式选择器
 * 使用类选择器current-paragraph作为被选中的样式选择器
 */
public class CustomLineNumberFactory implements IntFunction<Node> {

    private ObservableValue<Integer> currentParagraphProperty = null;
    private StyleClassedTextArea textArea = null;

    public CustomLineNumberFactory(StyleClassedTextArea textArea){
        this.currentParagraphProperty = textArea.currentParagraphProperty();
        this.textArea = textArea;
    }

    @Override
    public Node apply(int value) {
        HBox hBox = new HBox();
        hBox.setPrefWidth(40);
        hBox.getStyleClass().add("line-box");
        if (value == this.currentParagraphProperty.getValue()) {
            hBox.getStyleClass().add("current-paragraph");
        }
//        EventStreams.changesOf(currentParagraphProperty)
//                .filter(ch -> !ch.getNewValue().equals(ch.getOldValue()))
//
//                .subscribe(integerChange -> {
//                    Integer newValue = integerChange.getNewValue();
//                    Integer oldValue = integerChange.getOldValue();
//                    if (newValue == value) {
//                        hBox.getStyleClass().add("current-paragraph");
//                        //this.textArea.setParagraphStyle(newValue, Collections.singleton("current-paragraph"));
//                    }
//                    else if (oldValue == value){
//                        hBox.getStyleClass().remove("current-paragraph");
//                        //this.textArea.setParagraphStyle(oldValue, Collections.singleton("others-paragraph"));
//                    }
//                });
        Label lineNumberLabel = new Label();
        lineNumberLabel.setText(String.valueOf(value));
        hBox.getChildren().add(lineNumberLabel);
        return hBox;

    }
}
