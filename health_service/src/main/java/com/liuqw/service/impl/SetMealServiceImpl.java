package com.liuqw.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuqw.dao.SetMealDao;
import com.liuqw.entity.PageResult;
import com.liuqw.pojo.CheckGroup;
import com.liuqw.pojo.Setmeal;
import com.liuqw.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import utils.MessageConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    /**
     * 查询所有检查组(回显)
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return setMealDao.findAll();
    }

        /**
         * 套餐分页查询
         */
        @Override
        public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
            //设置分页参数
            PageHelper.startPage(currentPage, pageSize);//当前页码  每页显示多少条记录
            //需要被分页的语句查询（一定要写到第一行代码后面）
            List<Setmeal> setmealList = setMealDao.selectByCondition(queryString);//分页逻辑交给PageHelper插件搞定了  select * from table where 用户输入的条件 （limit ）
            //封装分页PageInfo对象
            PageInfo<Setmeal> pageInfo = new PageInfo<>(setmealList);
            return new PageResult(pageInfo.getTotal(), pageInfo.getList());
        }


    /**
     * 新增套餐数据
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //新增套餐表（单表插入）
        setMealDao.add(setmeal);
        Integer setmealId = setmeal.getId();///检查组id
        //往中间表插入数据(单独抽取一个方法 --- 后续编辑还会用此方法)
        setCheckGroupAndSetmeal(setmealId,checkgroupIds);
    }

    /**
     * 根据id查询套餐数据
     * @param id
     */
    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    /**
     * 根据套餐id查询检查组的ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckGroupAndSetmeal(Integer id) {
        return setMealDao.findCheckGroupAndSetmeal(id);
    }

    /**
     *  编辑套餐数据
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //1.根据套餐id删除套餐和检查组关系
        setMealDao.deleteReShip(setmeal.getId());
        //2.重新建立套餐和检查组关系
        setCheckGroupAndSetmeal(setmeal.getId(),checkgroupIds);
        //3.更新套餐数据
        setMealDao.edit(setmeal);
    }

    /**
     * 删除套餐数据
     */
    @Override
    public void delete(Integer id) {
        //1.根据套餐id查询 套餐检查组中间表
        int count = setMealDao.findSetmealAndCheckGroupBySetmealId(id);
        //2.存在记录 直接抛出异常
        if(count > 0){
            throw new RuntimeException(MessageConstant.ERROR_CHECKGROUP_SETMEAL);
        }
        //3.直接删除套餐数据
        setMealDao.delete(id);
    }

    /***
     * 往检查组和套餐中间表插入数据
     */
    public void setCheckGroupAndSetmeal(Integer setmealId,Integer[] checkgroupIds){
        if(checkgroupIds !=null && checkgroupIds.length>0 ){
            for (Integer checkgroupId : checkgroupIds) {
                //使用map来封装中间表的数据
                Map<String,Integer> map = new HashMap();
                map.put("checkGroupId",checkgroupId);
                map.put("setmealId",setmealId);
                setMealDao.setCheckGroupAndSetmeal(map);
            }
        }
    }
}
