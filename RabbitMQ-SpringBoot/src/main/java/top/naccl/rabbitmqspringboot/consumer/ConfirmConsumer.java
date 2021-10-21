package top.naccl.rabbitmqspringboot.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.naccl.rabbitmqspringboot.config.ConfirmConfig;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-21
 */
@Slf4j
@Component
public class ConfirmConsumer {
	@RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
	public void receiveConfirmMessage(Message message) {
		String msg = new String(message.getBody());
		log.info("接收到队列confirm.queue消息：{}", msg);
	}
}
