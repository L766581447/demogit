package com.liuqw.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.liuqw.entity.PageResult;
import com.liuqw.entity.QueryPageBean;
import com.liuqw.entity.Result;
import com.liuqw.pojo.CheckGroup;
import com.liuqw.pojo.Setmeal;
import com.liuqw.service.SetMealService;
import constent.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;
import utils.MessageConstant;
import utils.QiniuUtils;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询所有检查组(回显)
     */
    @RequestMapping(value = "/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> checkGroupList = setMealService.findAll();
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页查询套餐
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = setMealService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 套餐图片上传
     * 方式一：MultipartFile imgFile
     * 方式二：@RequestParam("imgFile") MultipartFile imgFile
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        try {
            // 获取图片MultipartFile 1.jpg
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();//1.jpg
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf);
            // 生成一个唯一的文件名称 UUID
            String fileName = UUID.randomUUID().toString() + suffix;
            // 最终调用七牛云接口
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //上传图片完成后 将图片名称记录到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS,fileName);//返回文件名（页面预览图片用户+提交表单图片名称）
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 新增套餐数据
     */
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setMealService.add(setmeal,checkgroupIds);
            //新增套餐数据完成后 将图片名称记录到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 根据id查询套餐数据
     * @return
     */
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 根据套餐id查询检查组的ids
     * @param id
     * @return
     */
    @RequestMapping(value = "/findCheckGroupAndSetmeal")
    public List<Integer> findCheckGroupAndSetmeal(Integer id){
        try {
            List<Integer> list = setMealService.findCheckGroupAndSetmeal(id);
            return list;
        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  编辑套餐数据
     */
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setMealService.edit(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        }  catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    /**
     * 删除套餐数据
     */
    @RequestMapping(value = "/delete")
    public Result delete(Integer id){
        try {
            setMealService.delete(id);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (RuntimeException e) { //捕获MessageConstant.ERROR_CHECKGROUP_SETMEAL
            e.printStackTrace();
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }
}
