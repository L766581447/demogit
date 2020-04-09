package com.liuqw.service;

import com.liuqw.entity.PageResult;
import com.liuqw.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
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

    /**
     * 检查项分页查询
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 编辑检查项
     */
    void edit(CheckItem checkItem);

}
