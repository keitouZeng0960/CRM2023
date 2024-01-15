package com.workspace.crm.workbench.web.controller;

import com.workspace.crm.commons.constants.Constants;
import com.workspace.crm.commons.pojo.ReturnObject;
import com.workspace.crm.commons.utils.DateUtils;
import com.workspace.crm.commons.utils.IdUtils;
import com.workspace.crm.settings.pojo.User;
import com.workspace.crm.workbench.pojo.MarketingActivitiesRemark;
import com.workspace.crm.workbench.service.ActivityRemarkService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    ActivityRemarkService activityRemarkService;


    //保存备注
    @RequestMapping("workbench/activity/saveRemark.do")
    @ResponseBody
    public Object saveRemark(String noteContent, String activityId, HttpSession session){
        MarketingActivitiesRemark remark=new MarketingActivitiesRemark();
        ReturnObject ro=new ReturnObject();
        //封装参数
        remark.setId(IdUtils.createId());
        remark.setNoteContent(noteContent);
        remark.setCreateTime(DateUtils.dateToString(new Date()));
        User user = (User)session.getAttribute(Constants.SYSTEM_USER_SESSION);
        remark.setCreateBy(user.getId());
        remark.setEditFlag("0");
        remark.setActivityId(activityId);
        //处理业务
        try {
            int count = activityRemarkService.saveRemark(remark);
            if(count!=1){
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("保存失败，请重试");
            }else {
                //保存成功，将刚才录入的备注查询出来返回给前台
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                ro.setRetData(remark);
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("保存失败，请重试");
        }
        return ro;
    }

    //根据备注id删除备注
    @RequestMapping("workbench/activity/deleteRemark.do")
    @ResponseBody
    public Object deleteRemark(String id){
        ReturnObject ro =new ReturnObject();
        try{
            int count = activityRemarkService.removeRemarkById(id);
            if(count>0){
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统异常，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统异常，请稍后重试");
        }
        return ro;
    }


    @RequestMapping("workbench/activity/updateRemark.do")
    @ResponseBody
    public Object updateRemark(String id,String noteContent,HttpSession session){
        ReturnObject ro= new ReturnObject();
        //封装参数
        MarketingActivitiesRemark remark = new MarketingActivitiesRemark();
        User user=(User)session.getAttribute(Constants.SYSTEM_USER_SESSION);
        remark.setId(id);
        remark.setNoteContent(noteContent);
        remark.setEditTime(DateUtils.dateToString(new Date()));
        remark.setEditBy(user.getId());
        remark.setEditFlag("1");
        try{
            int count = activityRemarkService.updateRemarkById(remark);
            if(count>0){
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                ro.setRetData(remark);
            }else{
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙，请重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙，请重试...");
        }
        return ro;
    }

}
