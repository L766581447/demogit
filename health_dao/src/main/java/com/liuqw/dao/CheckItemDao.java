package com.liuqw.dao;

import com.liuqw.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    List<CheckItem> findAll();

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    void add(CheckItem checkItem);

    /**
     * 删除检查项
     */
    void delete(Integer id);


    int findCountByCheckItemId(Integer id);

    /**
     * 检查项分页查询
     */
    List<CheckItem> selectByCondition(String queryString);

    /**
     * 编辑检查项
     */
    void edit(CheckItem checkItem);

}
