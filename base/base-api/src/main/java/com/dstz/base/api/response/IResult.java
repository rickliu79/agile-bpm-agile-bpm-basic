package com.dstz.base.api.response;

<<<<<<< HEAD
import java.io.Serializable;

/**
 * 返回结果
 *
 * @author Jeff
 */
public interface IResult extends Serializable {

    /**
     * 本地调用是否成功
     *
     * @return 是否成功
     */
    Boolean getIsOk();

    /**
     * 调用状态码
     *
     * @return 状态码
     */
    String getCode();

    /**
     * 调用信息
     *
     * @return 调用信息
     */
    String getMsg();

    /**
     * 调用出错堆栈信息
     *
     * @return 出错堆栈信息
     */
    String getCause();
=======
import com.dstz.base.api.constant.IStatusCode;

import java.io.Serializable;

public interface IResult extends Serializable {
    public abstract Boolean getIsOk();

    public abstract IStatusCode getStatusCode();

    public abstract String getCode();

    public abstract String getMsg();

    public abstract String getCause();
>>>>>>> branch 'master' of https://gitee.com/agile-bpm/agile-bpm-basic.git
}
