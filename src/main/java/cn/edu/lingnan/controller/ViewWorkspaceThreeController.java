package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.utils.R;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/2/17.
 */
public class ViewWorkspaceThreeController extends Controller {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        R.getConfig().currentTabIndexProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 3)
                this.reFlashChart();
        }));
    }

    public void reFlashChart(){
        System.out.println("通知的方式已经传达");
    }


}
