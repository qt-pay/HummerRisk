package com.hummerrisk.base.mapper;

import com.hummerrisk.base.domain.ImageRepo;
import com.hummerrisk.base.domain.ImageRepoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageRepoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    long countByExample(ImageRepoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    int deleteByExample(ImageRepoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    int insert(ImageRepo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    int insertSelective(ImageRepo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    List<ImageRepo> selectByExample(ImageRepoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    ImageRepo selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    int updateByExampleSelective(@Param("record") ImageRepo record, @Param("example") ImageRepoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    int updateByExample(@Param("record") ImageRepo record, @Param("example") ImageRepoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    int updateByPrimaryKeySelective(ImageRepo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table image_repo
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    int updateByPrimaryKey(ImageRepo record);
}
