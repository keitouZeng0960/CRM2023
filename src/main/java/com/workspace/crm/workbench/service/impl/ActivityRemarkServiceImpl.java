package com.workspace.crm.workbench.service.impl;

import com.workspace.crm.workbench.mapper.MarketingActivitiesRemarkMapper;
import com.workspace.crm.workbench.pojo.MarketingActivitiesRemark;
import com.workspace.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    MarketingActivitiesRemarkMapper mapper;

//    根据市场活动的id查询该市场活动下的所有备注
    @Override
    public List<MarketingActivitiesRemark> queryActivityAllRemarks(String id) {
        return mapper.queryActivityRemarksById(id);
    }

    @Override
    public int saveRemark(MarketingActivitiesRemark remark) {
        return mapper.insertRemark(remark);
    }

    @Override
    public MarketingActivitiesRemark queryRemarkById(String id) {
        return mapper.selectRemarkById(id);
    }

    @Override
    public int removeRemarkById(String id) {
        return mapper.deleteRemarkById(id);
    }

    @Override
    public int updateRemarkById(MarketingActivitiesRemark remark) {
        return mapper.updateRemarkById(remark);
    }
}
