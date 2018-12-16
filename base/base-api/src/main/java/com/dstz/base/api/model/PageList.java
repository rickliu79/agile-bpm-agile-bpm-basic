package com.dstz.base.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 包含“分页”信息的List，这个对象包含分页数据和分页结果。
 */
public class PageList<E> extends ArrayList<E> implements Serializable {

    private static final long serialVersionUID = 1412759446332294208L;

    public PageList() {
    }

    /**
     * 分页大小
     */
    private int limit = 12;
    /**
     * 页数
     */
    private int page = 1;
    /**
     * 总记录数
     */
    private int totalCount = 0;

    public PageList(int page, int limit, int totalCount, List<E> list) {
        super(list);
        this.limit = limit;
        this.totalCount = totalCount;
        this.page = computePageNo(page);
    }

    /**
     * 取得当前页。
     */
    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    /**
     * 取得总项数。
     *
     * @return 总项数
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 是否是首页（第一页），第一页页码为1
     *
     * @return 首页标识
     */
    public boolean isFirstPage() {
        return page <= 1;
    }

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
    public boolean isLastPage() {
        return page >= getTotalPages();
    }

    public int getPrePage() {
        if (isHasPrePage()) {
            return page - 1;
        } else {
            return page;
        }
    }

    public int getNextPage() {
        if (isHasNextPage()) {
            return page + 1;
        } else {
            return page;
        }
    }

    /**
     * 是否有上一页
     *
     * @return 上一页标识
     */
    public boolean isHasPrePage() {
        return (page - 1 >= 1);
    }

    /**
     * 是否有下一页
     *
     * @return 下一页标识
     */
    public boolean isHasNextPage() {
        return (page + 1 <= getTotalPages());
    }

    /**
     * 开始行，可以用于oracle分页使用 (1-based)。
     */
    public int getStartRow() {
        if (getLimit() <= 0 || totalCount <= 0) return 0;
        return page > 0 ? (page - 1) * getLimit() + 1 : 0;
    }

    /**
     * 结束行，可以用于oracle分页使用 (1-based)。
     */
    public int getEndRow() {
        return page > 0 ? Math.min(limit * page, getTotalCount()) : 0;
    }

    /**
     * offset，计数从0开始，可以用于mysql分页使用(0-based)
     */
    public int getOffset() {
        return page > 0 ? (page - 1) * getLimit() : 0;
    }


    /**
     * 得到 总页数
     *
     * @return
     */
    public int getTotalPages() {
        if (totalCount <= 0) {
            return 0;
        }
        if (limit <= 0) {
            return 0;
        }

        int count = totalCount / limit;
        if (totalCount % limit > 0) {
            count++;
        }
        return count;
    }

    protected int computePageNo(int page) {
        return computePageNumber(page, limit, totalCount);
    }

    
    private static int computeLastPageNumber(int totalItems, int pageSize) {
        if (pageSize <= 0) return 1;
        int result = (int) (totalItems % pageSize == 0 ?
                totalItems / pageSize
                : totalItems / pageSize + 1);
        if (result <= 1)
            result = 1;
        return result;
    }

    private static int computePageNumber(int page, int pageSize, int totalItems) {
        if (page <= 1) {
            return 1;
        }
        if (Integer.MAX_VALUE == page
                || page > computeLastPageNumber(totalItems, pageSize)) { //last page
            return computeLastPageNumber(totalItems, pageSize);
        }
        return page;
    }

}
