package cn.edu.lingnan.service.impl;

import cn.edu.lingnan.dao.ThemeDao;
import cn.edu.lingnan.dao.impl.ThemeDaoImpl;
import cn.edu.lingnan.pojo.Theme;
import cn.edu.lingnan.service.ThemeService;

import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 * Last Edited by Mechan on 2018/3/30.
 */
public class ThemeServiceImpl implements ThemeService{

    ThemeDao themeDao = new ThemeDaoImpl();

    //查询数据库Theme表的所有记录
    @Override
    public List<Theme> findAllRecord() {
        return themeDao.getListByHQL("from Theme");
    }
}
