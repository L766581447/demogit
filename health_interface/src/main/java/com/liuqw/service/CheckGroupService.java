package com.liuqw.service;

import com.liuqw.entity.PageResult;
import com.liuqw.pojo.CheckGroup;
import com.liuqw.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {

    /**
     * 分页查询检查组
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 查询所有检查项
     */
    List<CheckItem> findAll();

    /**
     * 新增检查组
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 根据检查组id查询检查组对象
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id查找检查项ids
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 删除检查组
     * @param id
     */
    void delete(Integer id);
}
