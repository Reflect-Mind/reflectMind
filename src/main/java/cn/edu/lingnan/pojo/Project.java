package cn.edu.lingnan.pojo;

import cn.edu.lingnan.controller.RootController;
import cn.edu.lingnan.utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/13.
 * 单一工程存储类
 */
public class Project {
    /**
     * 工程名称
     */
    private String projectName;



    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public static void main(String[] args){
        RootController rootController = R.getObjectFromFactory(RootController.class);
        System.out.println(rootController);
    }
}
