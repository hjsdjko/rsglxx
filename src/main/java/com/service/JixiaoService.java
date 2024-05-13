package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.JixiaoEntity;
import java.util.Map;

/**
 * 绩效 服务类
 * @author 
 * @since 2021-03-03
 */
public interface JixiaoService extends IService<JixiaoEntity> {

     PageUtils queryPage(Map<String, Object> params);

}