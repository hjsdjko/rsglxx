package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.XinziEntity;
import java.util.Map;

/**
 * 薪资 服务类
 * @author 
 * @since 2021-03-03
 */
public interface XinziService extends IService<XinziEntity> {

     PageUtils queryPage(Map<String, Object> params);

}