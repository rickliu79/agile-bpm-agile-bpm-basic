package com.dstz.sys.simplemq.handler.biz;

import java.io.Serializable;

import com.dstz.sys.api.jms.consumer.JmsConsumer;
/**
 * 做公共逻辑,如日志等
 * @author Jeff
 *
 */
public abstract class AbsBizMessageConsumer<T extends Serializable> implements JmsConsumer<T>{

}
