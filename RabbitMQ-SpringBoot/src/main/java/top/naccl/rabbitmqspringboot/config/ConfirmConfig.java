package top.naccl.rabbitmqspringboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
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

	//备份交换机
	public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
	//备份队列
	public static final String BACKUP_QUEUE_NAME = "backup_queue";
	//报警队列
	public static final String WARNING_QUEUE_NAME = "warning_queue";

	@Bean
	public Queue confirmQueue() {
		return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
	}

	@Bean
	public DirectExchange confirmExchange() {
		return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true).withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).build();
	}

//	@Bean
//	public CustomExchange confirmExchange() {
//		Map<String, Object> arguments = new HashMap<>();
//		arguments.put("x-delayed-type", "direct");
//		return new CustomExchange(CONFIRM_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
//	}

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

	//备份交换机
	@Bean
	public FanoutExchange backupExchange() {
		return new FanoutExchange(BACKUP_EXCHANGE_NAME);
	}

	@Bean
	public Queue backupQueue() {
		return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
	}

	@Bean
	public Queue warningQueue() {
		return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
	}

	@Bean
	public Binding backupQueueBindingExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
	                                          @Qualifier("backupQueue") Queue backupQueue) {
		return BindingBuilder.bind(backupQueue).to(backupExchange);
	}

	@Bean
	public Binding warningQueueBindingExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
	                                           @Qualifier("warningQueue") Queue warningQueue) {
		return BindingBuilder.bind(warningQueue).to(backupExchange);
	}
}
