package com.workspace.crm.workbench.mapper;

import com.workspace.crm.workbench.pojo.MarketingActivitiesRemark;

import java.util.List;

public interface MarketingActivitiesRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jan 07 08:13:06 JST 2024
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jan 07 08:13:06 JST 2024
     */
    int insert(MarketingActivitiesRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jan 07 08:13:06 JST 2024
     */
    int insertSelective(MarketingActivitiesRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jan 07 08:13:06 JST 2024
     */
    MarketingActivitiesRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jan 07 08:13:06 JST 2024
     */
    int updateByPrimaryKeySelective(MarketingActivitiesRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Jan 07 08:13:06 JST 2024
     */
    int updateByPrimaryKey(MarketingActivitiesRemark record);


    List<MarketingActivitiesRemark> queryActivityRemarksById(String id);

    int insertRemark(MarketingActivitiesRemark remark);


    //根据备注id查询备注信息
    MarketingActivitiesRemark selectRemarkById(String id);


    int deleteRemarkById(String id);


    int updateRemarkById(MarketingActivitiesRemark remark);
}