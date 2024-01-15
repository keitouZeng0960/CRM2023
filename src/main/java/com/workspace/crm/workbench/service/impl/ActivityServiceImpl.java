package com.workspace.crm.workbench.service.impl;

import com.workspace.crm.workbench.mapper.MarketingActivitiesMapper;
import com.workspace.crm.workbench.pojo.MarketingActivities;
import com.workspace.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private MarketingActivitiesMapper mapper;
    @Override
    public int saveActivity(MarketingActivities marketingActivities) {
        return mapper.insertActivity(marketingActivities);
    }

    @Override
    public List<MarketingActivities> queryActivityByConditionForPage(Map<String, Object> map) {
        List<MarketingActivities> activityList =mapper.selectActivityByConditionForPage(map);
        System.out.println(activityList);
        return activityList;

    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return mapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return mapper.deleteMarketingActivityByIds(ids);
    }

    @Override
    public MarketingActivities queryActivityByIdWhenUpdate(String id) {
        return mapper.selectActivityByIdWhenUpdate(id);
    }

    @Override
    public int saveEditActivity(MarketingActivities activities) {
        return mapper.updateActivityById(activities);
    }

    @Override
    public List<MarketingActivities> queryAllActivities() {
        return mapper.selectAllActivities();
    }

    @Override
    public List<MarketingActivities> queryActivitiesByIds(String[] ids) {return mapper.selectActivitiesByIds(ids);}


//   根据id查询单个市场活动
    @Override
    public MarketingActivities queryActivityById(String id) {
        return mapper.selectActivityById(id);
    }
}
