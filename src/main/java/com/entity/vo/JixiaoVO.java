package com.entity.vo;

import com.entity.JixiaoEntity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 绩效
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 * @author 
 * @email
 * @date 2021-03-03
 */
@TableName("jixiao")
public class JixiaoVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */

    @TableField(value = "id")
    private Integer id;


    /**
     * 用户
     */

    @TableField(value = "yonghu_id")
    private Integer yonghuId;


    /**
     * 月份
     */

    @TableField(value = "month")
    private String month;


    /**
     * 绩效类型
     */

    @TableField(value = "jixiao_types")
    private Integer jixiaoTypes;


    /**
     * 绩效内容
     */

    @TableField(value = "jixiao_content")
    private String jixiaoContent;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat

    @TableField(value = "create_time")
    private Date createTime;


    /**
	 * 设置：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 获取：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 设置：用户
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }


    /**
	 * 获取：用户
	 */

    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 设置：月份
	 */
    public String getMonth() {
        return month;
    }


    /**
	 * 获取：月份
	 */

    public void setMonth(String month) {
        this.month = month;
    }
    /**
	 * 设置：绩效类型
	 */
    public Integer getJixiaoTypes() {
        return jixiaoTypes;
    }


    /**
	 * 获取：绩效类型
	 */

    public void setJixiaoTypes(Integer jixiaoTypes) {
        this.jixiaoTypes = jixiaoTypes;
    }
    /**
	 * 设置：绩效内容
	 */
    public String getJixiaoContent() {
        return jixiaoContent;
    }


    /**
	 * 获取：绩效内容
	 */

    public void setJixiaoContent(String jixiaoContent) {
        this.jixiaoContent = jixiaoContent;
    }
    /**
	 * 设置：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 获取：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
