package com.yeahmobi.yscheduler.model.dao.mapper;

import com.yeahmobi.yscheduler.model.TaskAuthority;
import com.yeahmobi.yscheduler.model.TaskAuthorityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TaskAuthorityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    int countByExample(TaskAuthorityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    int deleteByExample(TaskAuthorityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    int insert(TaskAuthority record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    int insertSelective(TaskAuthority record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    List<TaskAuthority> selectByExampleWithRowbounds(TaskAuthorityExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    List<TaskAuthority> selectByExample(TaskAuthorityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    TaskAuthority selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") TaskAuthority record, @Param("example") TaskAuthorityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") TaskAuthority record, @Param("example") TaskAuthorityExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TaskAuthority record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_authority
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TaskAuthority record);
}