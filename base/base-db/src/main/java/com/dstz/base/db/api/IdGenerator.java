package com.dstz.base.db.api;

/**
 * ID主键产生器
 *
 * @author csx
 */
public interface IdGenerator {
    /**
     * 获取唯一主键ID
     *
     * @return 长整型
     */
    public Long getUId();

    /**
     * 获取
     *
     * @return
     */
    public String getSuid();
}
