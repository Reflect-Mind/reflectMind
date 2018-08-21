package cn.edu.lingnan.service.command;

import javafx.concurrent.Task;

/**
 * Created by Administrator on 2018/1/29.
 */
public abstract class AbstractCommand<V> extends Task<V>{

    protected V call() throws Exception {
        return null;
    }
}
