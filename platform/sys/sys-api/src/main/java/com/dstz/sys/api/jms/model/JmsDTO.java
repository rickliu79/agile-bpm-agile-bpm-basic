package com.dstz.sys.api.jms.model;

import java.io.Serializable;

public interface JmsDTO<T extends Serializable> extends Serializable{
	
	/**
	 * 具体消费者的标识
	 * @return
	 */
	String getType();
	
	/*
	 * 消费者的数据对象 
	 */
	public T getData() ;
}
