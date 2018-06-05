package com.dstz.sys.simplemq.handler.msg;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dstz.sys.api.jms.consumer.JmsConsumer;
import com.dstz.sys.api.jms.model.JmsDTO;

/**
 * 做消息类型的公共逻辑 ：如日志等
 * @author Jeff
 * @param <T>
 */
public abstract class AbsNotifyMessageHandler<T extends Serializable> implements JmsConsumer<T>{
	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	
	public abstract String getTitle();

	public boolean getIsDefault() {
		return false;
	}

	public boolean getSupportHtml() {
		return true;
	}
	
	
	 @Override
	 public boolean handlerMessage(JmsDTO<T> message) {
		 // 日志记录一下？？？
		 return  sendMessage(message.getData());
	 }

	 public abstract boolean sendMessage(T data);

}
