package com.liuqw.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuqw.dao.CheckItemDao;
import com.liuqw.entity.PageResult;
import com.liuqw.pojo.CheckItem;
import com.liuqw.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import utils.MessageConstant;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }


    /**
     * 删除检查项
     */
    @Override
    public void delete(Integer id) {
        // 1.查询检查项和检查组是否存在关系
        int count = checkItemDao.findCountByCheckItemId(id);
        // 2.如果有关系则不允许删除
        if(count > 0){
            throw new RuntimeException(MessageConstant.ERROR_CHECKGROUP_CHECKITEM);
        }
        // 3.如果没有关系可以删除
        checkItemDao.delete(id);
    }


    /**
     * 检查项分页查询
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //分页插件使用
        //设置分页参数
        PageHelper.startPage(currentPage,pageSize);//当前页码  每页显示多少条记录
        //需要被分页的语句查询（一定要写到第一行代码后面）
        List<CheckItem> checkItemList = checkItemDao.selectByCondition(queryString);//分页逻辑交给PageHelper插件搞定了  select * from table where 用户输入的条件 （limit ）
        //封装分页PageInfo对象
        PageInfo<CheckItem> pageInfo = new PageInfo<>(checkItemList);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 编辑检查项
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }
}
