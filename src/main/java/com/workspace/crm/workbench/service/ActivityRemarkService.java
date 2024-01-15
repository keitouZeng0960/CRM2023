package com.workspace.crm.workbench.service;

import com.workspace.crm.workbench.pojo.MarketingActivitiesRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<MarketingActivitiesRemark> queryActivityAllRemarks(String id);

    int saveRemark(MarketingActivitiesRemark remark);

    //通过备注id查询备注详细信息
    MarketingActivitiesRemark queryRemarkById(String id);

    int removeRemarkById(String id);

    int updateRemarkById(MarketingActivitiesRemark remark);
}
