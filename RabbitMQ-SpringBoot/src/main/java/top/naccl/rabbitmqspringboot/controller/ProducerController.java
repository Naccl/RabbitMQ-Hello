package top.naccl.rabbitmqspringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.rabbitmqspringboot.config.ConfirmConfig;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-21
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@GetMapping("/sendMsg/{message}")
	public void sendMsg(@PathVariable String message) {
		CorrelationData correlationData1 = new CorrelationData("1");
		rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData1);
		log.info("发送消息内容：{}", message);

		CorrelationData correlationData2 = new CorrelationData("2");
		rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME + "?", ConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData2);
		log.info("发送消息内容：{}", message);

		CorrelationData correlationData3 = new CorrelationData("3");
		rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY + "?", message, correlationData3);
		log.info("发送消息内容：{}", message);
	}
}
