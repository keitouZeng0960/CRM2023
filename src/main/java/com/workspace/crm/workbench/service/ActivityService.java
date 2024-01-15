package com.workspace.crm.workbench.service;

import com.workspace.crm.workbench.pojo.MarketingActivities;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    int saveActivity(MarketingActivities marketingActivities);

    List<MarketingActivities> queryActivityByConditionForPage(Map<String,Object> map);

    int queryCountOfActivityByCondition(Map<String,Object> map);


    int deleteActivityByIds(String[] ids);


    MarketingActivities queryActivityByIdWhenUpdate(String id);

    int saveEditActivity(MarketingActivities activities);

    List<MarketingActivities> queryAllActivities();

    List<MarketingActivities> queryActivitiesByIds(String[] ids);

    MarketingActivities queryActivityById(String id);
}
