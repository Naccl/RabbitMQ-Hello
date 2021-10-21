package top.naccl.rabbitmqspringboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-21
 */
@Configuration
public class ConfirmConfig {
	public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
	public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
	public static final String CONFIRM_ROUTING_KEY = "key1";

	@Bean
	public DirectExchange confirmExchange() {
		return new DirectExchange(CONFIRM_EXCHANGE_NAME);
	}

//	@Bean
//	public CustomExchange confirmExchange() {
//		Map<String, Object> arguments = new HashMap<>();
//		arguments.put("x-delayed-type", "direct");
//		/**
//		 * 1.交换机的名称
//		 * 2.交换机的类型
//		 * 3.是否需要持久化
//		 * 4.是否需要自动删除
//		 * 5.其它参数
//		 */
//		return new CustomExchange(CONFIRM_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
////		return new DirectExchange(CONFIRM_EXCHANGE_NAME);
//	}

	@Bean
	public Queue confirmQueue() {
		return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
	}

	@Bean
	public Binding queueBindingExchange(@Qualifier("confirmExchange") DirectExchange confirmExchange,
	                                    @Qualifier("confirmQueue") Queue confirmQueue) {
		return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
	}

//	@Bean
//	public Binding queueBindingExchange(@Qualifier("confirmExchange") CustomExchange confirmExchange,
//	                                    @Qualifier("confirmQueue") Queue confirmQueue) {
//		return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY).noargs();
//	}
}
