package cn.edu.lingnan.service;

import cn.edu.lingnan.pojo.Theme;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 * Last Edited by Mechan on 2018/3/30.
 */
public interface ThemeService {

    //查询数据库Theme表的所有记录
    public List<Theme> findAllRecord();

}
