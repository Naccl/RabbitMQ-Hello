package top.naccl.rabbitmqspringboot.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-21
 */
@Slf4j
@Component
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
	@Autowired
	RabbitTemplate rabbitTemplate;

	@PostConstruct
	public void init() {
		//也可以通过这种方式替代 spring.rabbitmq.publisher-returns=true
//		rabbitTemplate.setMandatory(true);
		//注入
		rabbitTemplate.setConfirmCallback(this);
		rabbitTemplate.setReturnsCallback(this);
	}

	/**
	 * 交换机确认回调接口
	 * 这个回调可以确认消息是否到达交换机
	 *
	 * @param correlationData 保存回调消息的id及相关信息
	 * @param ack             交换机是否收到消息
	 * @param cause           接收消息失败的原因
	 */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		String id = correlationData != null ? correlationData.getId() : "null";
		if (ack) {
			log.info("交换机已经收到id为:{}的消息", id);
		} else {
			log.error("交换机未收到id为:{}的消息，原因:{}", id, cause);
		}
	}

	/**
	 * 可以在当消息传递过程中不可达目的地时将消息返回给生产者，只有在消息不可达目的地的时候才进行回退
	 * 这个回调可以确认消息是否到达队列
	 *
	 * @param returned 回退的消息
	 */
	@Override
	public void returnedMessage(ReturnedMessage returned) {
		log.error("消息:{}，被交换机{}退回，原因:{}，路由Key:{}",
				new String(returned.getMessage().getBody()),
				returned.getExchange(),
				returned.getReplyText(),
				returned.getRoutingKey()
		);
	}
}
