package com.dstz.base.core.datatrans;

/**
 * <pre>
 * 描述：数据对象转换器。
 * 用户可以自行设计转换器。
 * </pre>
 */
public interface ITypeConvert {

    Object processValue(Object obj);

}
