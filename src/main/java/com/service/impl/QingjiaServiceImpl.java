package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.QingjiaDao;
import com.entity.QingjiaEntity;
import com.service.QingjiaService;
import com.entity.view.QingjiaView;

/**
 * 请假 服务实现类
 * @author 
 * @since 2021-03-03
 */
@Service("qingjiaService")
@Transactional
public class QingjiaServiceImpl extends ServiceImpl<QingjiaDao, QingjiaEntity> implements QingjiaService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        if(params != null && (params.get("limit") == null || params.get("page") == null)){
            params.put("page","1");
            params.put("limit","10");
        }
        Page<QingjiaView> page =new Query<QingjiaView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }

}
