package com.hummerrisk.base.mapper;

import com.hummerrisk.base.domain.FileSystemResultItem;
import com.hummerrisk.base.domain.FileSystemResultItemExample;
import com.hummerrisk.base.domain.FileSystemResultItemWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileSystemResultItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    long countByExample(FileSystemResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int deleteByExample(FileSystemResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int insert(FileSystemResultItemWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int insertSelective(FileSystemResultItemWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    List<FileSystemResultItemWithBLOBs> selectByExampleWithBLOBs(FileSystemResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    List<FileSystemResultItem> selectByExample(FileSystemResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    FileSystemResultItemWithBLOBs selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int updateByExampleSelective(@Param("record") FileSystemResultItemWithBLOBs record, @Param("example") FileSystemResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int updateByExampleWithBLOBs(@Param("record") FileSystemResultItemWithBLOBs record, @Param("example") FileSystemResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int updateByExample(@Param("record") FileSystemResultItem record, @Param("example") FileSystemResultItemExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int updateByPrimaryKeySelective(FileSystemResultItemWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int updateByPrimaryKeyWithBLOBs(FileSystemResultItemWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    int updateByPrimaryKey(FileSystemResultItem record);
}
