package com.hummerrisk.base.domain;

import java.io.Serializable;

public class CloudNativeResultItem implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.id
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.result_id
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String resultId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.title
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String title;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.vulnerability_id
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String vulnerabilityId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.severity
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String severity;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.score
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String score;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.target
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String target;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.primary_link
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String primaryLink;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.installed_version
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String installedVersion;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.fixed_version
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String fixedVersion;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.create_time
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.resource
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String resource;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.image
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String image;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_result_item.links
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private String links;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cloud_native_result_item
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.id
     *
     * @return the value of cloud_native_result_item.id
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.id
     *
     * @param id the value for cloud_native_result_item.id
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.result_id
     *
     * @return the value of cloud_native_result_item.result_id
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getResultId() {
        return resultId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.result_id
     *
     * @param resultId the value for cloud_native_result_item.result_id
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setResultId(String resultId) {
        this.resultId = resultId == null ? null : resultId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.title
     *
     * @return the value of cloud_native_result_item.title
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.title
     *
     * @param title the value for cloud_native_result_item.title
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.vulnerability_id
     *
     * @return the value of cloud_native_result_item.vulnerability_id
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getVulnerabilityId() {
        return vulnerabilityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.vulnerability_id
     *
     * @param vulnerabilityId the value for cloud_native_result_item.vulnerability_id
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setVulnerabilityId(String vulnerabilityId) {
        this.vulnerabilityId = vulnerabilityId == null ? null : vulnerabilityId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.severity
     *
     * @return the value of cloud_native_result_item.severity
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.severity
     *
     * @param severity the value for cloud_native_result_item.severity
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setSeverity(String severity) {
        this.severity = severity == null ? null : severity.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.score
     *
     * @return the value of cloud_native_result_item.score
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getScore() {
        return score;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.score
     *
     * @param score the value for cloud_native_result_item.score
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setScore(String score) {
        this.score = score == null ? null : score.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.target
     *
     * @return the value of cloud_native_result_item.target
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getTarget() {
        return target;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.target
     *
     * @param target the value for cloud_native_result_item.target
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setTarget(String target) {
        this.target = target == null ? null : target.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.primary_link
     *
     * @return the value of cloud_native_result_item.primary_link
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getPrimaryLink() {
        return primaryLink;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.primary_link
     *
     * @param primaryLink the value for cloud_native_result_item.primary_link
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setPrimaryLink(String primaryLink) {
        this.primaryLink = primaryLink == null ? null : primaryLink.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.installed_version
     *
     * @return the value of cloud_native_result_item.installed_version
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getInstalledVersion() {
        return installedVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.installed_version
     *
     * @param installedVersion the value for cloud_native_result_item.installed_version
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setInstalledVersion(String installedVersion) {
        this.installedVersion = installedVersion == null ? null : installedVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.fixed_version
     *
     * @return the value of cloud_native_result_item.fixed_version
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getFixedVersion() {
        return fixedVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.fixed_version
     *
     * @param fixedVersion the value for cloud_native_result_item.fixed_version
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setFixedVersion(String fixedVersion) {
        this.fixedVersion = fixedVersion == null ? null : fixedVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.create_time
     *
     * @return the value of cloud_native_result_item.create_time
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.create_time
     *
     * @param createTime the value for cloud_native_result_item.create_time
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.resource
     *
     * @return the value of cloud_native_result_item.resource
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getResource() {
        return resource;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.resource
     *
     * @param resource the value for cloud_native_result_item.resource
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setResource(String resource) {
        this.resource = resource == null ? null : resource.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.image
     *
     * @return the value of cloud_native_result_item.image
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getImage() {
        return image;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.image
     *
     * @param image the value for cloud_native_result_item.image
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_result_item.links
     *
     * @return the value of cloud_native_result_item.links
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public String getLinks() {
        return links;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_result_item.links
     *
     * @param links the value for cloud_native_result_item.links
     *
     * @mbg.generated Sun Nov 13 02:41:59 CST 2022
     */
    public void setLinks(String links) {
        this.links = links == null ? null : links.trim();
    }
}