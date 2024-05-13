package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.RenwuEntity;
import java.util.Map;

/**
 * 任务 服务类
 * @author 
 * @since 2021-03-03
 */
public interface RenwuService extends IService<RenwuEntity> {

     PageUtils queryPage(Map<String, Object> params);

}