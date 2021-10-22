package top.naccl.rabbitmqspringboot.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.naccl.rabbitmqspringboot.config.ConfirmConfig;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-22
 */
@Slf4j
@Component
public class WarningConsumer {
	@RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
	public void receiveWarningMessage(Message message) {
		String msg = new String(message.getBody());
		log.error("发现不可路由的消息:{}", msg);
	}
}
