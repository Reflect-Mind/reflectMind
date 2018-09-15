package cn.edu.lingnan.command;

import javafx.concurrent.Task;

/**
 * Created by Administrator on 2018/1/29.
 * @author 李田锋
 */
public abstract class AbstractCommand<V> extends Task<V>{

    @Override
    protected V call() throws Exception {
        return null;
    }
}
