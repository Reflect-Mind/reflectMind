package cn.edu.lingnan.service.command;

import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import cn.edu.lingnan.utils.HibernateSessionFactory;
import cn.edu.lingnan.utils.PreferencesUtils;
import cn.edu.lingnan.utils.R;
import cn.edu.lingnan.view.DialogView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import org.controlsfx.dialog.ProgressDialog;

import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/1/29.
 * 系统初始化
 * 负责相关参数的加载(字典...)
 * 恢复控制器上一次的工作状态由拦截器负责
 * 程序初始化步骤：
 * <ul>
 *     <li>查看全局设置中的参数basePath是否已经设置,该路径将是今后存放的项目的根目录，
 *     当没有发现全局设置中没有被设置时,程序要跳出窗口来上用户设置项目的根目录
 *     </li>
 *     <li>
 *         出现进度条窗口，显示各项基础配置的加载情况
 *     </li>
 *     <li>
 *         各项配置加载完毕后,正式进入工作界面
 *     </li>
 * </ul>
 */
public class InitCommand extends AbstractCommand<Integer>{

    /**
     * 初始化后台各项数据的任务
     * @return Task实例
     */
    private Task<Void> loadingTask(){
        Task<Void> task =  new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                this.updateMessage("正在加载局部配置...");
                R.getConfig();
                this.updateMessage("正在加载控制器工厂...");
                R.getObjectFromFactory(null);
                this.updateMessage("正在加载快捷编辑机器人");
                R.getRobot();
                this.updateMessage("正在加载数据库...");
                HibernateSessionFactory.init();
                //this.updateMessage("注册全局事件监听");

                this.updateMessage("加载完成...");
                //Thread.sleep(2000);
                return null;
            }
        };
        Executors.newSingleThreadExecutor().execute(task);
        return task;
    }
    protected Integer call() throws Exception {

        Platform.runLater(() -> {
            String basePath =  PreferencesUtils.getParametersAsString("basePath");
            DialogView dialogView = new DialogView();
            if (basePath == null || basePath.equals("")) {
                TextInputDialog dialog = dialogView.showTextInputDialog();
                Optional<String> stringOptional = dialog.showAndWait();
                if (stringOptional.isPresent()) {
                    basePath = stringOptional.get();
                    //PreferencesUtils.setParametersAsString("basePath", null);
                }
            }
            //依然为空,则将跳出程序
            if(basePath == null)
                return;
            Task task = this.loadingTask();
            ProgressDialog progressDialog = new ProgressDialog(task);
            progressDialog.showAndWait();
            R.getApplication().getStage().show();
        });

        return null;
    }
}
