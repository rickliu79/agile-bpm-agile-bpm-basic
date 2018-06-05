package com.dstz.sys.api.jms.consumer;

import java.io.Serializable;

import com.dstz.sys.api.jms.model.JmsDTO;

/**
 * <pre>
 * 所有消费者 均需要实现该接口
 * @Type 通过type 获取具体的处理者
 * </pre>
 * @author jeff
 */
public interface JmsConsumer<T extends Serializable> {
	 String getType();
	 boolean handlerMessage(JmsDTO<T> message);
}
