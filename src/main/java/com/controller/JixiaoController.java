package com.controller;


import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.StringUtil;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import com.entity.JixiaoEntity;

import com.service.JixiaoService;
import com.entity.view.JixiaoView;
import com.service.YonghuService;
import com.entity.YonghuEntity;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 绩效
 * 后端接口
 * @author
 * @email
 * @date 2021-03-03
*/
@RestController
@Controller
@RequestMapping("/jixiao")
public class JixiaoController {
    private static final Logger logger = LoggerFactory.getLogger(JixiaoController.class);

    @Autowired
    private JixiaoService jixiaoService;


    @Autowired
    private TokenService tokenService;


    //级联表service
    @Autowired
    private YonghuService yonghuService;

    //字典表map
    Map<String, Map<Integer, String>> dictionaryMap;

    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
    logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(StringUtil.isNotEmpty(role) && "用户".equals(role)){
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        }
    PageUtils page = jixiaoService.queryPage(params);

    //字典表数据转换
    List<JixiaoView> list =(List<JixiaoView>)page.getList();
        ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        dictionaryMap = (Map<String, Map<Integer, String>>) servletContext.getAttribute("dictionaryMap");
        for(JixiaoView c:list){
        this.dictionaryConvert(c);
        }
        return R.ok().put("data", page);
    }
    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        JixiaoEntity jixiao = jixiaoService.selectById(id);
        if(jixiao !=null){
            //entity转view
            JixiaoView view = new JixiaoView();
            BeanUtils.copyProperties( jixiao , view );//把实体数据重构到view中

            //级联表
            YonghuEntity yonghu = yonghuService.selectById(jixiao.getYonghuId());
            if(yonghu != null){
                BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                view.setYonghuId(yonghu.getId());
            }
            //字典表字典转换
            ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
            dictionaryMap = (Map<String, Map<Integer, String>>) servletContext.getAttribute("dictionaryMap");
            this.dictionaryConvert(view);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody JixiaoEntity jixiao, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,jixiao:{}",this.getClass().getName(),jixiao.toString());
        Wrapper<JixiaoEntity> queryWrapper = new EntityWrapper<JixiaoEntity>()
            .eq("yonghu_id", jixiao.getYonghuId())
            .eq("month", jixiao.getMonth())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        JixiaoEntity jixiaoEntity = jixiaoService.selectOne(queryWrapper);
        if(jixiaoEntity==null){
            jixiao.setCreateTime(new Date());
        //  String role = String.valueOf(request.getSession().getAttribute("role"));
        //  if("".equals(role)){
        //      jixiao.set
        //  }
            jixiaoService.insert(jixiao);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody JixiaoEntity jixiao, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,jixiao:{}",this.getClass().getName(),jixiao.toString());
        //根据字段查询是否有相同数据
        Wrapper<JixiaoEntity> queryWrapper = new EntityWrapper<JixiaoEntity>()
            .notIn("id",jixiao.getId())
            .eq("yonghu_id", jixiao.getYonghuId())
            .eq("month", jixiao.getMonth())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        JixiaoEntity jixiaoEntity = jixiaoService.selectOne(queryWrapper);
        if(jixiaoEntity==null){
            //  String role = String.valueOf(request.getSession().getAttribute("role"));
            //  if("".equals(role)){
            //      jixiao.set
            //  }
            jixiaoService.updateById(jixiao);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }


    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        jixiaoService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
    *字典表数据转换
    */
    public void dictionaryConvert(JixiaoView jixiaoView){
        //当前表的字典字段
        if(StringUtil.isNotEmpty(String.valueOf(jixiaoView.getJixiaoTypes()))){
            jixiaoView.setJixiaoValue(dictionaryMap.get("jixiao_types").get(jixiaoView.getJixiaoTypes()));
        }

        //级联表的字典字段
        if(StringUtil.isNotEmpty(String.valueOf(jixiaoView.getBumenTypes()))){
            jixiaoView.setBumenValue(dictionaryMap.get("bumen_types").get(jixiaoView.getBumenTypes()));
        }
        if(StringUtil.isNotEmpty(String.valueOf(jixiaoView.getZhiweiTypes()))){
            jixiaoView.setZhiweiValue(dictionaryMap.get("zhiwei_types").get(jixiaoView.getZhiweiTypes()));
        }
        if(StringUtil.isNotEmpty(String.valueOf(jixiaoView.getSexTypes()))){
            jixiaoView.setSexValue(dictionaryMap.get("sex_types").get(jixiaoView.getSexTypes()));
        }
        if(StringUtil.isNotEmpty(String.valueOf(jixiaoView.getPoliticsTypes()))){
            jixiaoView.setPoliticsValue(dictionaryMap.get("politics_types").get(jixiaoView.getPoliticsTypes()));
        }
    }


}

