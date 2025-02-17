package com.hummerrisk.base.domain;

import java.io.Serializable;

public class FileSystem implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.name
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.file_name
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String fileName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.plugin_icon
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String pluginIcon;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.status
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.create_time
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.update_time
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private Long updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.creator
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String creator;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.size
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String size;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.path
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String path;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.dir
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String dir;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.sbom_id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String sbomId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.sbom_version_id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private String sbomVersionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column file_system.proxy_id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private Integer proxyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table file_system
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.id
     *
     * @return the value of file_system.id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.id
     *
     * @param id the value for file_system.id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.name
     *
     * @return the value of file_system.name
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.name
     *
     * @param name the value for file_system.name
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.file_name
     *
     * @return the value of file_system.file_name
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.file_name
     *
     * @param fileName the value for file_system.file_name
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.plugin_icon
     *
     * @return the value of file_system.plugin_icon
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getPluginIcon() {
        return pluginIcon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.plugin_icon
     *
     * @param pluginIcon the value for file_system.plugin_icon
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setPluginIcon(String pluginIcon) {
        this.pluginIcon = pluginIcon == null ? null : pluginIcon.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.status
     *
     * @return the value of file_system.status
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.status
     *
     * @param status the value for file_system.status
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.create_time
     *
     * @return the value of file_system.create_time
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.create_time
     *
     * @param createTime the value for file_system.create_time
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.update_time
     *
     * @return the value of file_system.update_time
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.update_time
     *
     * @param updateTime the value for file_system.update_time
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.creator
     *
     * @return the value of file_system.creator
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.creator
     *
     * @param creator the value for file_system.creator
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.size
     *
     * @return the value of file_system.size
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getSize() {
        return size;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.size
     *
     * @param size the value for file_system.size
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.path
     *
     * @return the value of file_system.path
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getPath() {
        return path;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.path
     *
     * @param path the value for file_system.path
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.dir
     *
     * @return the value of file_system.dir
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getDir() {
        return dir;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.dir
     *
     * @param dir the value for file_system.dir
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setDir(String dir) {
        this.dir = dir == null ? null : dir.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.sbom_id
     *
     * @return the value of file_system.sbom_id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getSbomId() {
        return sbomId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.sbom_id
     *
     * @param sbomId the value for file_system.sbom_id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setSbomId(String sbomId) {
        this.sbomId = sbomId == null ? null : sbomId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.sbom_version_id
     *
     * @return the value of file_system.sbom_version_id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public String getSbomVersionId() {
        return sbomVersionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.sbom_version_id
     *
     * @param sbomVersionId the value for file_system.sbom_version_id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setSbomVersionId(String sbomVersionId) {
        this.sbomVersionId = sbomVersionId == null ? null : sbomVersionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column file_system.proxy_id
     *
     * @return the value of file_system.proxy_id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public Integer getProxyId() {
        return proxyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column file_system.proxy_id
     *
     * @param proxyId the value for file_system.proxy_id
     *
     * @mbg.generated Fri Oct 14 23:54:40 CST 2022
     */
    public void setProxyId(Integer proxyId) {
        this.proxyId = proxyId;
    }
}