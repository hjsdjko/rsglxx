package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ChuqinEntity;
import java.util.Map;

/**
 * 考勤 服务类
 * @author 
 * @since 2021-03-03
 */
public interface ChuqinService extends IService<ChuqinEntity> {

     PageUtils queryPage(Map<String, Object> params);

}