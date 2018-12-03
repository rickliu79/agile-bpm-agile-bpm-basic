package com.dstz.sys.simplemq.producer;

import com.dstz.sys.api.jms.constants.JmsDestinationConstant;
import com.dstz.sys.api.jms.model.JmsDTO;
import com.dstz.sys.api.jms.producer.JmsProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * Redis消息队列提供实现
 *
 * @author wacxhs
 */
public class RedisMessageQueueProducer implements JmsProducer {

    @Autowired
    private RedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public void sendToQueue(JmsDTO message) {
        redisTemplate.boundListOps(JmsDestinationConstant.DEFAULT_NAME).rightPush(message);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sendToQueue(List<JmsDTO> messages) {
        redisTemplate.boundListOps(JmsDestinationConstant.DEFAULT_NAME).rightPushAll(messages.toArray());
    }
}
