package top.naccl.rabbitmqspringboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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
 * @Date: 2021-10-19
 */
@Configuration
public class TtlQueueConfig {
	//普通交换机名称
	public static final String EXCHANGE_X = "X";
	//死信交换机名称
	public static final String DEAD_LETTER_EXCHANGE_Y = "Y";
	//普通队列名称
	public static final String QUEUE_A = "QA";
	public static final String QUEUE_B = "QB";
	//死信队列名称
	public static final String DEAD_LETTER_QUEUE_D = "QD";
	//普通队列A routingKey
	public static final String QUEUE_A_ROUTING_KEY = "XA";
	//普通队列B routingKey
	public static final String QUEUE_B_ROUTING_KEY = "XB";
	//死信队列D routingKey
	public static final String DEAD_LETTER_QUEUE_D_ROUTING_KEY = "YD";

	public static final String QUEUE_C = "QC";
	public static final String QUEUE_C_ROUTING_KEY = "XC";

	/**
	 * 声明普通队列QC 不设置TTL
	 */
	@Bean("queueC")
	public Queue queueC() {
		Map<String, Object> arguments = new HashMap<>();
		//设置死信交换机
		arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_Y);
		//设置死信routingKey
		arguments.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_D_ROUTING_KEY);
		return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
	}

	/**
	 * 绑定QC和X
	 */
	@Bean
	public Binding queueCBindingX(@Qualifier("queueC") Queue queueC, @Qualifier("exchangeX") DirectExchange exchangeX) {
		return BindingBuilder.bind(queueC).to(exchangeX).with(QUEUE_C_ROUTING_KEY);
	}

	/**
	 * 声明交换机X
	 */
	@Bean("exchangeX")
	public DirectExchange exchangeX() {
		return new DirectExchange(EXCHANGE_X);
	}

	/**
	 * 声明交换机Y
	 */
	@Bean("exchangeY")
	public DirectExchange exchangeY() {
		return new DirectExchange(DEAD_LETTER_EXCHANGE_Y);
	}

	/**
	 * 声明普通队列QA TTL为10s
	 */
	@Bean("queueA")
	public Queue queueA() {
		Map<String, Object> arguments = new HashMap<>();
		//设置死信交换机
		arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_Y);
		//设置死信routingKey
		arguments.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_D_ROUTING_KEY);
		//设置TTL 单位ms
		arguments.put("x-message-ttl", 10000);
		return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
	}

	/**
	 * 声明普通队列QB TTL为40s
	 */
	@Bean("queueB")
	public Queue queueB() {
		Map<String, Object> arguments = new HashMap<>();
		//设置死信交换机
		arguments.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_Y);
		//设置死信routingKey
		arguments.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_D_ROUTING_KEY);
		//设置TTL 单位ms
		arguments.put("x-message-ttl", 40000);
		return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
	}

	/**
	 * 声明死信队列
	 */
	@Bean("queueD")
	public Queue queueD() {
		return QueueBuilder.durable(DEAD_LETTER_QUEUE_D).build();
	}

	/**
	 * 绑定QA和X
	 */
	@Bean
	public Binding queueABindingX(@Qualifier("queueA") Queue queueA, @Qualifier("exchangeX") DirectExchange exchangeX) {
		return BindingBuilder.bind(queueA).to(exchangeX).with(QUEUE_A_ROUTING_KEY);
	}

	/**
	 * 绑定QB和X
	 */
	@Bean
	public Binding queueBBindingX(@Qualifier("queueB") Queue queueB, @Qualifier("exchangeX") DirectExchange exchangeX) {
		return BindingBuilder.bind(queueB).to(exchangeX).with(QUEUE_B_ROUTING_KEY);
	}

	/**
	 * 绑定QD和Y
	 */
	@Bean
	public Binding queueDBindingY(@Qualifier("queueD") Queue queueD, @Qualifier("exchangeY") DirectExchange exchangeY) {
		return BindingBuilder.bind(queueD).to(exchangeY).with(DEAD_LETTER_QUEUE_D_ROUTING_KEY);
	}
}
