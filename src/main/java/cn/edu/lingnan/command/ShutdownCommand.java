package cn.edu.lingnan.command;

import cn.edu.lingnan.utils.R;

/**
 * Created by Administrator on 2018/2/26.
 * 程序关闭时被启动
 */
public class ShutdownCommand extends AbstractCommand {

    @Override
    protected Object call() throws Exception {
        try {
            R.shutdownAndWrite();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
