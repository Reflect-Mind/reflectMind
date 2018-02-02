package cn.edu.lingnan.sdk.overlay;
import cn.edu.lingnan.sdk.CGLibProxy.*;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.utils.R;
import javafx.fxml.FXMLLoader;

import java.io.File;

/**
 * Created by Administrator on 2018/1/27.
 */
public class CustomXMLLoader extends FXMLLoader {
    //默认拦截器
    public CustomXMLLoader(){
        this.setControllerFactory((param) ->{
            Object target = null;
            target = R.getObjectFromFactory(param);
            if (target == null)
                try {
                    target = param.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            return target;
        });
    }

}
