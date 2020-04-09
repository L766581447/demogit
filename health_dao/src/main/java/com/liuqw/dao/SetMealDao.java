package com.liuqw.dao;

import com.liuqw.pojo.CheckGroup;
import com.liuqw.pojo.Setmeal;


import java.util.List;
import java.util.Map;


public interface SetMealDao {

    /**
     * 查询所有检查组(回显)
     * @return
     */
    List<CheckGroup> findAll();

    /**
     * 套餐分页查询
     */
    List<Setmeal> selectByCondition(String queryString);

    /**
     * 新增套餐数据
     */
    void add(Setmeal setmeal);

    /**
     * 往检查组和套餐中间表插入数据
     * @param map
     */
    void setCheckGroupAndSetmeal(Map<String, Integer> map);

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
     * 根据套餐id删除套餐和检查组关系
     * @param id
     */
    void deleteReShip(Integer id);

    /**
     * 编辑套餐
     * @param setmeal
     */
    void edit(Setmeal setmeal);

    /**
     * 根据套餐id查询 套餐检查组中间表
     * @param id
     * @return
     */
    int findSetmealAndCheckGroupBySetmealId(Integer id);

    /**
     * 删除套餐数据
     */
    void delete(Integer id);
}
