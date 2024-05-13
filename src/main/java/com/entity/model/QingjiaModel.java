package com.entity.model;

import com.entity.QingjiaEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;


/**
 * 请假
 * 接收传参的实体类
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 * @author 
 * @email
 * @date 2021-03-03
 */
public class QingjiaModel implements Serializable {
    private static final long serialVersionUID = 1L;




    /**
     * 主键
     */
    private Integer id;


    /**
     * 用户
     */
    private Integer yonghuId;


    /**
     * 请假开始时间
     */
    private String onday;


    /**
     * 请假结束时间
     */
    private String downday;


    /**
     * 请假天数
     */
    private Integer qingjiaNumber;


    /**
     * 请假状态
     */
    private Integer qingjiaTypes;


    /**
     * 理由
     */
    private String liyouContent;


    /**
     * 审批时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date updateTime;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 设置：主键
	 */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：用户
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }


    /**
	 * 设置：用户
	 */
    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 获取：请假开始时间
	 */
    public String getOnday() {
        return onday;
    }


    /**
	 * 设置：请假开始时间
	 */
    public void setOnday(String onday) {
        this.onday = onday;
    }
    /**
	 * 获取：请假结束时间
	 */
    public String getDownday() {
        return downday;
    }


    /**
	 * 设置：请假结束时间
	 */
    public void setDownday(String downday) {
        this.downday = downday;
    }
    /**
	 * 获取：请假天数
	 */
    public Integer getQingjiaNumber() {
        return qingjiaNumber;
    }


    /**
	 * 设置：请假天数
	 */
    public void setQingjiaNumber(Integer qingjiaNumber) {
        this.qingjiaNumber = qingjiaNumber;
    }
    /**
	 * 获取：请假状态
	 */
    public Integer getQingjiaTypes() {
        return qingjiaTypes;
    }


    /**
	 * 设置：请假状态
	 */
    public void setQingjiaTypes(Integer qingjiaTypes) {
        this.qingjiaTypes = qingjiaTypes;
    }
    /**
	 * 获取：理由
	 */
    public String getLiyouContent() {
        return liyouContent;
    }


    /**
	 * 设置：理由
	 */
    public void setLiyouContent(String liyouContent) {
        this.liyouContent = liyouContent;
    }
    /**
	 * 获取：审批时间
	 */
    public Date getUpdateTime() {
        return updateTime;
    }


    /**
	 * 设置：审批时间
	 */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
	 * 获取：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 设置：创建时间
	 */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    }
