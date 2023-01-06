package com.hummerrisk.base.mapper;

import com.hummerrisk.base.domain.CloudNative;
import com.hummerrisk.base.domain.CloudNativeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CloudNativeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    long countByExample(CloudNativeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int deleteByExample(CloudNativeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int insert(CloudNative record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int insertSelective(CloudNative record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    List<CloudNative> selectByExampleWithBLOBs(CloudNativeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    List<CloudNative> selectByExample(CloudNativeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    CloudNative selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int updateByExampleSelective(@Param("record") CloudNative record, @Param("example") CloudNativeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int updateByExampleWithBLOBs(@Param("record") CloudNative record, @Param("example") CloudNativeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int updateByExample(@Param("record") CloudNative record, @Param("example") CloudNativeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int updateByPrimaryKeySelective(CloudNative record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int updateByPrimaryKeyWithBLOBs(CloudNative record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native
     *
     * @mbg.generated Thu Dec 22 22:45:23 CST 2022
     */
    int updateByPrimaryKey(CloudNative record);
}
