package top.naccl.rabbitmqspringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.rabbitmqspringboot.config.DelayedQueueConfig;
import top.naccl.rabbitmqspringboot.config.TtlQueueConfig;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-19
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@GetMapping("/sendMsg/{message}")
	public void sendMsg(@PathVariable String message) {
		log.info("当前时间:{}, 发送一条消息给两个TTL队列:{}", LocalDateTime.now(), message);
		rabbitTemplate.convertAndSend(TtlQueueConfig.EXCHANGE_X, TtlQueueConfig.QUEUE_A_ROUTING_KEY, "消息来自TTL为10s的队列:" + message);
		rabbitTemplate.convertAndSend(TtlQueueConfig.EXCHANGE_X, TtlQueueConfig.QUEUE_B_ROUTING_KEY, "消息来自TTL为40s的队列:" + message);
	}

	@GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
	public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
		log.info("当前时间:{}, 发送一条TTL为{}ms的消息给队列QC:{}", LocalDateTime.now(), ttlTime, message);
		rabbitTemplate.convertAndSend(TtlQueueConfig.EXCHANGE_X, TtlQueueConfig.QUEUE_C_ROUTING_KEY, message, msg -> {
			//设置消息TTL
			msg.getMessageProperties().setExpiration(ttlTime);
			return msg;
		});
	}

	@GetMapping("/sendDelayMsg/{message}/{delayTime}")
	public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
		log.info("当前时间:{}, 发送一条TTL为{}ms的消息给延迟队列delayed.queue:{}", LocalDateTime.now(), delayTime, message);
		rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
			//发送消息时 设置延迟时长 单位ms
			msg.getMessageProperties().setDelay(delayTime);
			return msg;
		});
	}
}
