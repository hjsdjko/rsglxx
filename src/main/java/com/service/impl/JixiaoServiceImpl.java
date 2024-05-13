package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.JixiaoDao;
import com.entity.JixiaoEntity;
import com.service.JixiaoService;
import com.entity.view.JixiaoView;

/**
 * 绩效 服务实现类
 * @author 
 * @since 2021-03-03
 */
@Service("jixiaoService")
@Transactional
public class JixiaoServiceImpl extends ServiceImpl<JixiaoDao, JixiaoEntity> implements JixiaoService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        if(params != null && (params.get("limit") == null || params.get("page") == null)){
            params.put("page","1");
            params.put("limit","10");
        }
        Page<JixiaoView> page =new Query<JixiaoView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }

}
