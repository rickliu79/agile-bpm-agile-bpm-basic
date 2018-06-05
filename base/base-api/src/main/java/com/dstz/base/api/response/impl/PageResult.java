package com.dstz.base.api.response.impl;

import com.dstz.base.api.model.PageList;


/**
 * 分页结果
 *
 * @param <E>
 */
public class PageResult<E> extends BaseResult {

    public PageResult(PageList<E> data) {
        super();
        this.data = data;
    }

    public PageResult(String errorMsg) {
        super(errorMsg);
    }

    public PageResult() {
        super();
    }

    private PageList<E> data;

    public PageList<E> getData() {
        return data;
    }

    public void setData(PageList<E> data) {
        this.data = data;
    }
}
