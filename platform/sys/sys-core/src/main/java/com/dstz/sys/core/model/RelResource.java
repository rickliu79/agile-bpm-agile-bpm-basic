package com.dstz.sys.core.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.dstz.base.api.model.IDModel;


/**
 * 关联资源 实体对象
 */
public class RelResource implements IDModel {

    /**
     * 主键
     */
    protected String id;

    /**
     * 资源ID
     */
    protected String resId;

    /**
     * 名称
     */
    protected String name;

    /**
     * 资源地址
     */
    protected String resUrl;


    public void setId(String id) {
        this.id = id;
    }

    /**
     * 返回 主键
     *
     * @return
     */
    public String getId() {
        return this.id;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    /**
     * 返回 资源ID
     *
     * @return
     */
    public String getResId() {
        return this.resId;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 返回 名称
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    /**
     * 返回 资源地址
     *
     * @return
     */
    public String getResUrl() {
        return this.resUrl;
    }

}