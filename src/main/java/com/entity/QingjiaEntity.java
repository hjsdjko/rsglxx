package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 请假
 *
 * @author 
 * @email
 * @date 2021-03-03
 */
@TableName("qingjia")
public class QingjiaEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public QingjiaEntity() {

	}

	public QingjiaEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")

    private Integer id;


    /**
     * 用户
     */
    @TableField(value = "yonghu_id")

    private Integer yonghuId;


    /**
     * 请假开始时间
     */
    @TableField(value = "onday")

    private String onday;


    /**
     * 请假结束时间
     */
    @TableField(value = "downday")

    private String downday;


    /**
     * 请假天数
     */
    @TableField(value = "qingjia_number")

    private Integer qingjiaNumber;


    /**
     * 请假状态
     */
    @TableField(value = "qingjia_types")

    private Integer qingjiaTypes;


    /**
     * 理由
     */
    @TableField(value = "liyou_content")

    private String liyouContent;


    /**
     * 审批时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @TableField(value = "update_time",fill = FieldFill.UPDATE)

    private Date updateTime;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @TableField(value = "create_time",fill = FieldFill.INSERT)

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
	 * 设置：请假开始时间
	 */
    public String getOnday() {
        return onday;
    }


    /**
	 * 获取：请假开始时间
	 */

    public void setOnday(String onday) {
        this.onday = onday;
    }
    /**
	 * 设置：请假结束时间
	 */
    public String getDownday() {
        return downday;
    }


    /**
	 * 获取：请假结束时间
	 */

    public void setDownday(String downday) {
        this.downday = downday;
    }
    /**
	 * 设置：请假天数
	 */
    public Integer getQingjiaNumber() {
        return qingjiaNumber;
    }


    /**
	 * 获取：请假天数
	 */

    public void setQingjiaNumber(Integer qingjiaNumber) {
        this.qingjiaNumber = qingjiaNumber;
    }
    /**
	 * 设置：请假状态
	 */
    public Integer getQingjiaTypes() {
        return qingjiaTypes;
    }


    /**
	 * 获取：请假状态
	 */

    public void setQingjiaTypes(Integer qingjiaTypes) {
        this.qingjiaTypes = qingjiaTypes;
    }
    /**
	 * 设置：理由
	 */
    public String getLiyouContent() {
        return liyouContent;
    }


    /**
	 * 获取：理由
	 */

    public void setLiyouContent(String liyouContent) {
        this.liyouContent = liyouContent;
    }
    /**
	 * 设置：审批时间
	 */
    public Date getUpdateTime() {
        return updateTime;
    }


    /**
	 * 获取：审批时间
	 */

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    @Override
    public String toString() {
        return "Qingjia{" +
            "id=" + id +
            ", yonghuId=" + yonghuId +
            ", onday=" + onday +
            ", downday=" + downday +
            ", qingjiaNumber=" + qingjiaNumber +
            ", qingjiaTypes=" + qingjiaTypes +
            ", liyouContent=" + liyouContent +
            ", updateTime=" + updateTime +
            ", createTime=" + createTime +
        "}";
    }
}
