package cn.edu.lingnan.service;

import cn.edu.lingnan.pojo.Category;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */
public interface CategoryService {

    //查询Category表中所有的记录
    public List<Category> findAllRecord();

}
