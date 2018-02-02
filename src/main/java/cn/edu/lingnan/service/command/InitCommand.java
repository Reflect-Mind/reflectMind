package cn.edu.lingnan.service.command;

/**
 * Created by Administrator on 2018/1/29.
 * 系统初始化
 * 负责相关参数的加载(字典...)
 * 恢复控制器上一次的工作状态由拦截器负责
 */
public class InitCommand extends AbstractCommand<Integer>{

    protected Integer call() throws Exception {
        System.out.println("执行完毕");
        return null;
    }
}
