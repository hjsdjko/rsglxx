package com.controller;


import java.text.DateFormat;
import java.text.ParseException;
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

import com.entity.ChuqinEntity;

import com.service.ChuqinService;
import com.entity.view.ChuqinView;
import com.service.YonghuService;
import com.entity.YonghuEntity;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 考勤
 * 后端接口
 * @author
 * @email
 * @date 2021-02-25
 */
@RestController
@Controller
@RequestMapping("/chuqin")
public class ChuqinController {
    private static final Logger logger = LoggerFactory.getLogger(ChuqinController.class);

    @Autowired
    private ChuqinService chuqinService;


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
        PageUtils page = chuqinService.queryPage(params);

        //字典表数据转换
        List<ChuqinView> list =(List<ChuqinView>)page.getList();
        ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        dictionaryMap = (Map<String, Map<Integer, String>>) servletContext.getAttribute("dictionaryMap");
        for(ChuqinView c:list){
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
        ChuqinEntity chuqin = chuqinService.selectById(id);
        if(chuqin !=null){
            //entity转view
            ChuqinView view = new ChuqinView();
            BeanUtils.copyProperties( chuqin , view );//把实体数据重构到view中

            //级联表
            YonghuEntity yonghu = yonghuService.selectById(chuqin.getYonghuId());
            if(yonghu != null){
                BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
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
     * 打卡
     */
    @RequestMapping("/clockIn")
    public R clockIn(String flag, HttpServletRequest request){
        logger.debug("clockIn方法:,,Controller:{},,flag:{}",this.getClass().getName(),flag);
        try {
            Integer id = (Integer)request.getSession().getAttribute("userId");
            String role = String.valueOf(request.getSession().getAttribute("role"));
            if(StringUtil.isEmpty(role) || "管理员".equals(role)){
                return R.error(511,"没有打卡权限");
            }
            Date d = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String date = format1.format(d);
            List<ChuqinEntity> chuqinList = new ArrayList<ChuqinEntity>();//要生成的list数据
            List<String> s = new ArrayList<>();
            s.add("yonghu_id+0");
            Wrapper<ChuqinEntity> queryWrapper3 = new EntityWrapper<ChuqinEntity>().eq("yonghu_id",id).orderDesc(s);
            List<ChuqinEntity> oldChuqinList = chuqinService.selectList(queryWrapper3);
            if("1".equals(flag)){
                //上班卡
                Date date1 = new Date();
                date1.setHours(8);
                date1.setMinutes(0);
                date1.setSeconds(0);
                //上班打卡
                //新增前先查看当前用户最大打卡时间

                if(oldChuqinList != null && oldChuqinList.size()>0){
                    ChuqinEntity entity = oldChuqinList.get(0);
                    String today = entity.getToday();//获取出勤表最大出勤
                    //把日期加一天
                    Date todayTime = format1.parse(today);
                    Calendar   calendar = new GregorianCalendar();
                    calendar.setTime(todayTime);
                    calendar.add(calendar.DATE,1);
                    String newToday = format1.format(calendar.getTime());
                    if(date.equals(today)){
                        return R.error(511,"已经打过上班卡了");
                    }else if(!date.equals(newToday)){//当天日期 不是出勤最大日期加一天的话   就是补充缺勤日期
                        chuqinList = this.getQueQin(d, format1, today, id);
                    }

                    if(chuqinList !=null && chuqinList.size() >0){
                        chuqinService.insertBatch(chuqinList);
                    }


                    //插入当天的上班卡
                    ChuqinEntity chuqin = new ChuqinEntity();
                    chuqin.setOnTime(d);
                    if(d.compareTo(date1)>0){//当前时间d 大于规定时间date1
                        chuqin.setChuqinTypes(6);//设置为迟到
                    }else if(d.compareTo(date1)<=0){//当前时间d 小于等于规定时间date1
                        chuqin.setChuqinTypes(3);//设置为未打下班卡
                    }
                    chuqin.setCreateTime(d);
                    chuqin.setToday(date);
                    chuqin.setYonghuId(id);
                    chuqinService.insert(chuqin);
                }else{
                    //第一次打卡
                    ChuqinEntity chuqin = new ChuqinEntity();
                    chuqin.setOnTime(d);
                    if(d.compareTo(date1)>0){//当前时间d 大于规定时间date1
                        chuqin.setChuqinTypes(6);//设置为迟到
                    }else if(d.compareTo(date1)<=0){//当前时间d 小于等于规定时间date1
                        chuqin.setChuqinTypes(3);//设置为未打下班卡
                    }
                    chuqin.setCreateTime(d);
                    chuqin.setToday(date);
                    chuqin.setYonghuId(id);
                    chuqinService.insert(chuqin);
                }

            }else if("2".equals(flag)){
                //下班打卡的地方
                Date date1 = new Date();
                date1.setHours(19);
                date1.setMinutes(00);
                date1.setSeconds(0);
                Date date2 = new Date();
                date2.setHours(18);
                date2.setMinutes(00);
                date2.setSeconds(0);
                //下班打卡
                if(oldChuqinList!=null){//不是第一次打卡
                    //查询当前用户是否生成了上班打卡
                    Wrapper<ChuqinEntity> queryWrapper = new EntityWrapper<ChuqinEntity>().eq("yonghu_id",id).eq("today",date).orderDesc(s);
                    ChuqinEntity chuqinEntity = chuqinService.selectOne(queryWrapper);
                    if(chuqinEntity !=null){//生成了上班打卡
                        chuqinEntity.setDownTime(d);
                        if("6".equals(String.valueOf(chuqinEntity.getChuqinTypes()))){
                        }else if(d.compareTo(date1)>0){//当前时间d 大于规定时间   加班
                            int hours = d.getHours();
                            int i = hours - 19+1;
                            if(i>0){
                                chuqinEntity.setOvertimeNumber(i);
                            }
                            chuqinEntity.setChuqinTypes(5);//设置为迟到
                        }else if(d.compareTo(date2)<0){//当前时间d 小于等于规定时间 早退
                            chuqinEntity.setChuqinTypes(7);//设置为未打下班卡
                        }else{
                            chuqinEntity.setChuqinTypes(1);
                        }
                        chuqinService.updateById(chuqinEntity);
                    }else{
                        //当天上午没有生成上班打卡,要防止用户昨天及之前没有生成打卡记录
                        Wrapper<ChuqinEntity> queryWrapper1 = new EntityWrapper<ChuqinEntity>().eq("yonghu_id",id).orderDesc(s);
                        List<ChuqinEntity> list = chuqinService.selectList(queryWrapper1);
                        if(list != null && list.size()>0){
                            ChuqinEntity entity = list.get(0);
                            String today = entity.getToday();//获取出勤表最大出勤
                            Date todayTime = format1.parse(today);
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(todayTime);
                            calendar.add(calendar.DATE,1);
                            String newToday = format1.format(calendar.getTime());
                            if(date.equals(today)){
                                //昨天id+1  等于今天的话  就是直接新增下午打卡
                                ChuqinEntity chuqin = new ChuqinEntity();
                                chuqin.setDownTime(d);
                                chuqin.setChuqinTypes(2);
                                chuqinService.insert(chuqin);
                            }else if(!date.equals(newToday)){//当天日期 不是出勤最大日期加一天的话   就是补充缺勤日期
                                chuqinList = this.getQueQin(d, format1, today, id);
                            }

                            if(chuqinList !=null && chuqinList.size() >0){
                                chuqinService.insertBatch(chuqinList);
                            }
                        }
                    }
                }else{
                    //第一次打卡
                    ChuqinEntity chuqin = new ChuqinEntity();
                    chuqin.setDownTime(d);
                    chuqin.setChuqinTypes(2);
                    chuqinService.insert(chuqin);
                }
            }else {
                return R.error(511,"未知错误");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok();
    }
//
//
//
// /**
//    * 后端保存
//    */
//    @RequestMapping("/save")
//    public R save(@RequestBody ChuqinEntity chuqin, HttpServletRequest request){
//        logger.debug("save方法:,,Controller:{},,chuqin:{}",this.getClass().getName(),chuqin.toString());
//        Wrapper<ChuqinEntity> queryWrapper = new EntityWrapper<ChuqinEntity>()
//            .eq("yonghu_id", chuqin.getYonghuId())
//            .eq("today", chuqin.getToday())
//            .eq("chuqin_types", chuqin.getChuqinTypes())
//            .eq("overtimeNumber", chuqin.getOvertimeNumber())
//            ;
//        logger.info("sql语句:"+queryWrapper.getSqlSegment());
//        ChuqinEntity chuqinEntity = chuqinService.selectOne(queryWrapper);
//        if(chuqinEntity==null){
//            chuqin.setCreateTime(new Date());
//        //  String role = String.valueOf(request.getSession().getAttribute("role"));
//        //  if("".equals(role)){
//        //      chuqin.set
//        //  }
//            chuqinService.insert(chuqin);
//            return R.ok();
//        }else {
//            return R.error(511,"表中有相同数据");
//        }
//    }
//
//    /**
//    * 修改
//    */
//    @RequestMapping("/update")
//    public R update(@RequestBody ChuqinEntity chuqin, HttpServletRequest request){
//        logger.debug("update方法:,,Controller:{},,chuqin:{}",this.getClass().getName(),chuqin.toString());
//        //根据字段查询是否有相同数据
//        Wrapper<ChuqinEntity> queryWrapper = new EntityWrapper<ChuqinEntity>()
//            .notIn("id",chuqin.getId())
//            .eq("yonghu_id", chuqin.getYonghuId())
//            .eq("today", chuqin.getToday())
//            .eq("chuqin_types", chuqin.getChuqinTypes())
//            .eq("overtimeNumber", chuqin.getOvertimeNumber())
//            ;
//        logger.info("sql语句:"+queryWrapper.getSqlSegment());
//        ChuqinEntity chuqinEntity = chuqinService.selectOne(queryWrapper);
//                chuqin.setOnTime(new Date());
//                chuqin.setDownTime(new Date());
//        if(chuqinEntity==null){
//            //  String role = String.valueOf(request.getSession().getAttribute("role"));
//            //  if("".equals(role)){
//            //      chuqin.set
//            //  }
//            chuqinService.updateById(chuqin);//根据id更新
//            return R.ok();
//        }else {
//            return R.error(511,"表中有相同数据");
//        }
//    }

    /**
     *
     * @param d   当前日期
     * @param format1 "yyyy-MM-dd"
     * @param newToday 数据库存的最大打卡日期 加一天
     * @param id    打卡人id
     * @return
     * @throws ParseException
     */
    public static  List<ChuqinEntity> getQueQin(Date d,SimpleDateFormat format1,String newToday,Integer id) throws ParseException {
        List<ChuqinEntity> list = new ArrayList<>();
        // 返回的日期集合
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = dateFormat.parse(newToday);
        Calendar   calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(calendar.DATE,-1); //当前时间减去一天，即一天前的时间
        Date end = dateFormat.parse(format1.format(calendar.getTime()));

        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
        while (tempStart.before(tempEnd)) {
            ChuqinEntity chuqinEntity = new ChuqinEntity();
            chuqinEntity.setYonghuId(id);
            chuqinEntity.setToday(dateFormat.format(tempStart.getTime()));
            chuqinEntity.setCreateTime(d);
            chuqinEntity.setChuqinTypes(4);
            list.add(chuqinEntity);
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return list;
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        chuqinService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
    *字典表数据转换
    */
    public void dictionaryConvert(ChuqinView chuqinView){
        //当前表的字典字段
        if(StringUtil.isNotEmpty(String.valueOf(chuqinView.getChuqinTypes()))){
            chuqinView.setChuqinValue(dictionaryMap.get("chuqin_types").get(chuqinView.getChuqinTypes()));
        }

        //级联表的字典字段
        if(StringUtil.isNotEmpty(String.valueOf(chuqinView.getBumenTypes()))){
            chuqinView.setBumenValue(dictionaryMap.get("bumen_types").get(chuqinView.getBumenTypes()));
        }
        if(StringUtil.isNotEmpty(String.valueOf(chuqinView.getZhiweiTypes()))){
            chuqinView.setZhiweiValue(dictionaryMap.get("zhiwei_types").get(chuqinView.getZhiweiTypes()));
        }
        if(StringUtil.isNotEmpty(String.valueOf(chuqinView.getSexTypes()))){
            chuqinView.setSexValue(dictionaryMap.get("sex_types").get(chuqinView.getSexTypes()));
        }
        if(StringUtil.isNotEmpty(String.valueOf(chuqinView.getPoliticsTypes()))){
            chuqinView.setPoliticsValue(dictionaryMap.get("politics_types").get(chuqinView.getPoliticsTypes()));
        }
    }


}

