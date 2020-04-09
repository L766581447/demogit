package com.liuqw.service;

import com.liuqw.entity.PageResult;
import com.liuqw.pojo.CheckGroup;
import com.liuqw.pojo.Setmeal;


import java.util.List;

public interface SetMealService {

    /**
     * 查询所有检查组(回显)
     */
    List<CheckGroup> findAll();

    /**
     * 分页查询套餐
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 新增套餐数据
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 根据id查询套餐数据
     */
    Setmeal findById(Integer id);

    /**
     * 根据套餐id查询检查组的ids
     * @param id
     * @return
     */
    List<Integer> findCheckGroupAndSetmeal(Integer id);

    /**
     *  编辑套餐数据
     */
    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 删除套餐数据
     */
    void delete(Integer id);
}
