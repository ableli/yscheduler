package com.yeahmobi.yscheduler.model.dao.mapper;

import com.yeahmobi.yscheduler.model.Workflow;
import com.yeahmobi.yscheduler.model.WorkflowExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface WorkflowMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    int countByExample(WorkflowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    int deleteByExample(WorkflowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    int insert(Workflow record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    int insertSelective(Workflow record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    List<Workflow> selectByExampleWithRowbounds(WorkflowExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    List<Workflow> selectByExample(WorkflowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    Workflow selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Workflow record, @Param("example") WorkflowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Workflow record, @Param("example") WorkflowExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Workflow record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table workflow
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Workflow record);
}