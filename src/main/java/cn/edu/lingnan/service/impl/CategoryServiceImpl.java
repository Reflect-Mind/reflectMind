package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.CategoryDao;
import cn.edu.lingnan.dao.impl.CategoryDaoImpl;
import cn.edu.lingnan.pojo.Category;
import cn.edu.lingnan.service.CategoryService;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 */
public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    //查询Category表中所有的记录
    @Override
    public List<Category> findAllRecord() {
        return categoryDao.getListByHQL("from Category");
    }
}
