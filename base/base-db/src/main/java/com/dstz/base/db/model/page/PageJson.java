package com.dstz.base.db.model.page;

import java.util.List;

import com.github.pagehelper.Page;

@SuppressWarnings("rawtypes")
public class PageJson {
    // 总记录数
    private Integer pageSize = 0;
    private Integer page = 1;
    private Integer total = 0;


    private PageResult pageResult = null;

    // 行记录

    private List rows = null;

    public PageJson() {

    }

    public PageJson(List rows, Integer total) {
        this.rows = rows;
        this.total = total;
    }

    public PageJson(List arrayList) {
        this.rows = arrayList;
        if (arrayList instanceof Page) {
        	Page pageList = (Page) arrayList;
        	Integer total = new Long(pageList.getTotal()).intValue();
        	this.pageSize = pageList.getPageSize();
            this.setPage(pageList.getPages());
            this.setPageResult(new PageResult(pageList.getPageNum(), pageList.getPageSize(),total));
           this.setTotal(total);
        	
        } else this.total = arrayList.size();
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * pageResult
     *
     * @return the pageResult
     * @since 1.0.0
     */

    public PageResult getPageResult() {
        return pageResult;
    }

    public void setPageResult(PageResult pageResult) {
        this.pageResult = pageResult;
    }

}
