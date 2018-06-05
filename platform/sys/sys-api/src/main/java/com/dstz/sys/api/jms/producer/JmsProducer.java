package com.dstz.sys.api.jms.producer;

import java.util.List;

import com.dstz.sys.api.jms.model.JmsDTO;

public interface JmsProducer {

	void sendToQueue(JmsDTO message);

	void sendToQueue(List<JmsDTO> jmsDto);

}