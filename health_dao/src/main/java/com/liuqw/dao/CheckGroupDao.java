package com.liuqw.dao;

import com.liuqw.pojo.CheckGroup;
import com.liuqw.pojo.CheckItem;
import org.apache.zookeeper.data.Id;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    /**
     * 检查组分页查询
     */
    List<CheckGroup> selectByCondition(String queryString);

    /**
     * 查询所有检查项
     */
    List<CheckItem> findAll();


    /**
     * 新增检查组
     */
    void add(CheckGroup checkGroup);

    /**
     * 往检查组和检查项的中间表插入数据
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> list);

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
     */
    void edit(CheckGroup checkGroup);

    /**
     * 删除检查组和检查项关系
     * @param id
     */
    void deleteReShip(Integer id);

    /**
     * 删除检查组
     */
    void delete(Integer id);

    /**
     * 根据检查组id 查询检查组和检查项中间表
     * @param id
     * @return
     */
    int findCheckGroupAndCheckItemByGroupId(Integer id);

    /**
     * 根据检查组id查询套餐和检查组中间表
     * @param id
     * @return
     */
    int findCheckGroupAndSetmealByGroupId(Integer id);
}
