package com.workspace.crm.workbench.web.controller;

import com.workspace.crm.commons.constants.Constants;
import com.workspace.crm.commons.pojo.ReturnObject;
import com.workspace.crm.commons.utils.DateUtils;
import com.workspace.crm.commons.utils.IdUtils;
import com.workspace.crm.settings.pojo.User;
import com.workspace.crm.settings.service.UserService;
import com.workspace.crm.workbench.pojo.MarketingActivities;
import com.workspace.crm.workbench.pojo.MarketingActivitiesRemark;
import com.workspace.crm.workbench.service.ActivityRemarkService;
import com.workspace.crm.workbench.service.ActivityService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {
    @Autowired
    UserService userService;
    @Autowired
    ActivityService activityService;
    @Autowired
    ActivityRemarkService activityRemarkService;

    //跳转到save页面，并动态显示用户信息
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //查出所有的用户数据，只显示没有处于锁定状态的用户
        List<User> users = userService.selectAllUsers();
        request.setAttribute("userList",users);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/toIndex.do")
    @ResponseBody
    //从浏览器接收表单的数据，并保存到数据库
    public Object toIndex(MarketingActivities activity, HttpSession session){
        ReturnObject ro =new ReturnObject();
        User user =(User) session.getAttribute(Constants.SYSTEM_USER_SESSION);
        //封装参数,前端传过来的数据不足，需要自己添加
        activity.setId(IdUtils.createId());
        activity.setCreateTime(DateUtils.dateToString(new Date()));
        activity.setCreateBy(user.getId());
        //调用service层
        //对于查数据，不需要捕捉异常，对于改底层数据需要捕捉异常
        try {
            int num = activityService.saveActivity(activity);
            if(num!=1){
                //存储失败
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("输入信息有误，登录失败");
            }else {
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }
        }catch (Exception e){
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统忙，请稍后重试....");
        }
        return ro;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String owner,String startDate, String endDate,String name,Integer pageNo,Integer pageSize){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("name",name);
        map.put("pageSize",pageSize);
        map.put("beginNo",(pageNo-1)*pageSize);
        //调用业务
        List<MarketingActivities> activityList= activityService.queryActivityByConditionForPage(map);
        int countNo =activityService.queryCountOfActivityByCondition(map);
        //返回参数，返回一个Map
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("activityList",activityList);
        resultMap.put("countNo",countNo);
        return resultMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        ReturnObject ro =new ReturnObject();
        //接收参数（前端发回来的id数组）
        //处理业务（根据id删除数据、判断是否成功）
        //执行数据库的增删改应该捕捉异常
        try{
            int count = activityService.deleteActivityByIds(id);
            if(count>0){
                //删除成功
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统异常，请稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统异常，请稍后再试");
        }
        //返回结果
        return ro;
    }

    @RequestMapping("/workbench/activity/queryActivityByIdWhenUpdate.do")
    @ResponseBody
    public Object queryActivityByIdWhenUpdate(String id){
        return activityService.queryActivityByIdWhenUpdate(id);
    }

    @RequestMapping("/workbench/activity/saveEditedActivity.do")
    @ResponseBody
    public Object saveEditedActivity(MarketingActivities activities,HttpSession session) {
        ReturnObject ro = new ReturnObject();
        User user = (User)session.getAttribute(Constants.SYSTEM_USER_SESSION);
        //封装参数
        activities.setEditBy(user.getId());
        activities.setEditTime(DateUtils.dateToString(new Date()));
        //处理业务
        try{
            int count = activityService.saveEditActivity(activities);
            if(count!=1){
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("查询出错");
            }else{
                //成功
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙，请稍后再试...");
        }
        return ro;
    }


    @RequestMapping("/workbench/activity/exportActivities.do")
    public void exportActivities(HttpServletResponse response)throws Exception{
        List<MarketingActivities> activityList = activityService.queryAllActivities();
        //创建excel文件
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell01 = row.createCell(0);
        cell01.setCellValue("id");
        HSSFCell cell02 = row.createCell(1);
        cell02.setCellValue("owner");
        HSSFCell cell03 = row.createCell(2);
        cell03.setCellValue("name");
        HSSFCell cell04 = row.createCell(3);
        cell04.setCellValue("start date");
        HSSFCell cell05 = row.createCell(4);
        cell05.setCellValue("end date");
        HSSFCell cell06 = row.createCell(5);
        cell06.setCellValue("cost");
        HSSFCell cell07 = row.createCell(6);
        cell07.setCellValue("description");
        HSSFCell cell08 = row.createCell(7);
        cell08.setCellValue("create time");
        HSSFCell cell09 = row.createCell(8);
        cell09.setCellValue("create by");

        if(activityList!=null && !activityList.isEmpty()){
            for(int i=0;i<activityList.size();i++){
                HSSFRow rows = sheet.createRow(i+1);
                MarketingActivities activity = activityList.get(i);
                rows.createCell(0).setCellValue(activity.getId());
                rows.createCell(1).setCellValue(activity.getOwner());
                rows.createCell(2).setCellValue(activity.getName());
                rows.createCell(3).setCellValue(activity.getStartDate());
                rows.createCell(4).setCellValue(activity.getEndDate());
                rows.createCell(5).setCellValue(activity.getCost());
                rows.createCell(6).setCellValue(activity.getDescription());
                rows.createCell(7).setCellValue(activity.getCreateTime());
                rows.createCell(8).setCellValue(activity.getCreateBy());
            }
        }

        //下载文件
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    @RequestMapping("/workbench/activity/exportActivitiesByIds.do")
    public void exportActivitiesByIds(String[] ids,HttpServletResponse response)throws Exception{
        List<MarketingActivities> activities = activityService.queryActivitiesByIds(ids);
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell01 = row.createCell(0);
        cell01.setCellValue("id");
        HSSFCell cell02 = row.createCell(1);
        cell02.setCellValue("owner");
        HSSFCell cell03 = row.createCell(2);
        cell03.setCellValue("name");
        HSSFCell cell04 = row.createCell(3);
        cell04.setCellValue("start date");
        HSSFCell cell05 = row.createCell(4);
        cell05.setCellValue("end date");
        HSSFCell cell06 = row.createCell(5);
        cell06.setCellValue("cost");
        HSSFCell cell07 = row.createCell(6);
        cell07.setCellValue("description");
        HSSFCell cell08 = row.createCell(7);
        cell08.setCellValue("create time");
        HSSFCell cell09 = row.createCell(8);
        cell09.setCellValue("create by");

        if(activities!=null && !activities.isEmpty()){
            for(int i=0;i<activities.size();i++){
                HSSFRow rows = sheet.createRow(i+1);
                MarketingActivities activity = activities.get(i);
                rows.createCell(0).setCellValue(activity.getId());
                rows.createCell(1).setCellValue(activity.getOwner());
                rows.createCell(2).setCellValue(activity.getName());
                rows.createCell(3).setCellValue(activity.getStartDate());
                rows.createCell(4).setCellValue(activity.getEndDate());
                rows.createCell(5).setCellValue(activity.getCost());
                rows.createCell(6).setCellValue(activity.getDescription());
                rows.createCell(7).setCellValue(activity.getCreateTime());
                rows.createCell(8).setCellValue(activity.getCreateBy());
            }
        }

        //下载文件
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=selectedActivityList.xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
    }

    @RequestMapping("workbench/activity/query/ActivityByIdForRemark.do")
    public String queryActivityByIdForRemark(String id,HttpServletRequest request){
        MarketingActivities activity = activityService.queryActivityById(id);
        List<MarketingActivitiesRemark> remarks = activityRemarkService.queryActivityAllRemarks(id);
        request.setAttribute("activity",activity);
        request.setAttribute("remarks",remarks);
        return "workbench/activity/detail";
    }



}
