package com.liuqw.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuqw.dao.CheckGroupDao;
import com.liuqw.entity.PageResult;
import com.liuqw.pojo.CheckGroup;
import com.liuqw.pojo.CheckItem;
import com.liuqw.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import utils.MessageConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 检查组分页查询
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //分页插件使用
        //设置分页参数
        PageHelper.startPage(currentPage, pageSize);//当前页码  每页显示多少条记录
        //需要被分页的语句查询（一定要写到第一行代码后面）
        List<CheckGroup> checkGroupList = checkGroupDao.selectByCondition(queryString);//分页逻辑交给PageHelper插件搞定了  select * from table where 用户输入的条件 （limit ）
        //封装分页PageInfo对象
        PageInfo<CheckGroup> pageInfo = new PageInfo<>(checkGroupList);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }


    /**
     * 查询所有检查项
     */
    @Override
    public List<CheckItem> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 新增检查组
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组表单
        checkGroupDao.add(checkGroup);
        //获得检查组的id
        Integer groupId = checkGroup.getId();
        //往中间表插入数据
        setCheckGroupAndCheckItem(groupId,checkitemIds);
    }


    /**
     * 根据检查组id查询检查组对象
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id查找检查项ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //1.	更新检查组表 update
        checkGroupDao.edit(checkGroup);
        //2.	删除检查组和检查项关系
        checkGroupDao.deleteReShip(checkGroup.getId());
        //3.	重新建立检查组和检查项关系（检查组id +  检查项ids 页面勾选）
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    /**
     * 删除检查组
     * @param id
     */
    @Override
    public void delete(Integer id) {
        // 根据检查组id 查询检查组和检查项中间表
        int c1 = checkGroupDao.findCheckGroupAndCheckItemByGroupId(id);
        // Count>0  提示不能删除
        if(c1>0){
            throw  new RuntimeException(MessageConstant.ERROR_CHECKGROUP_CHECKITEM);
        }
        // Count =0  根据检查组id查询套餐和检查组中间表
        int c2 = checkGroupDao.findCheckGroupAndSetmealByGroupId(id);
        // Count>0 提示不能删除检查组
        if(c2 > 0){
            throw  new RuntimeException(MessageConstant.ERROR_CHECKGROUP_SETMEAL);
        }
        //删除检查组
        checkGroupDao.delete(id);
    }


    /**
     * 往检查组和检查项的中间表插入数据
     */
    private void setCheckGroupAndCheckItem(Integer groupId, Integer[] checkitemIds) {
        for (Integer checkitemId : checkitemIds) {
            Map<String,Integer> list = new HashMap<>();
            list.put("groupId",groupId);
            list.put("itemId",checkitemId);
            checkGroupDao.setCheckGroupAndCheckItem(list);
        }
    }
}
