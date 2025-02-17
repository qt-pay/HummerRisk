package com.hummerrisk.oss.controller.request;

import com.hummerrisk.base.domain.OssBucket;

import java.util.Map;

/**
 * harris
 */
public class OssBucketRequest extends OssBucket {

    private String name;

    private String bucketId;

    private Map<String, Object> combine;

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getCombine() {
        return combine;
    }

    public void setCombine(Map<String, Object> combine) {
        this.combine = combine;
    }
}
