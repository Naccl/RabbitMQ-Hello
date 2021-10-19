package top.naccl.rabbitmq.dead;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-17
 */
public class Consumer01 {
	//普通交换机名称
	public static final String NORMAL_EXCHANGE = "normal_exchange";
	//死信交换机名称
	public static final String DEAD_EXCHANGE = "dead_exchange";
	//普通队列名称
	public static final String NORMAL_QUEUE = "normal_queue";
	//死信队列名称
	public static final String DEAD_QUEUE = "dead_queue";
	//普通队列routingKey
	public static final String NORMAL_ROUTING_KEY = "normal_routing_key";
	//死信队列routingKey
	public static final String DEAD_ROUTING_KEY = "dead_routing_key";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();
		//声明死信和普通交换机
		channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
		channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

		//设置普通队列参数
		Map<String, Object> arguments = new HashMap<>();
		//设置队列中消息过期时间 或在生产者指定消息过期时间
//		arguments.put("x-message-ttl", 10000);
		//正常队列设置死信交换机
		arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
		//设置死信routingKey
		arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
		//设置普通队列长度的限制
//		arguments.put("x-max-length", 6);

		//声明普通队列
		channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

		//声明死信队列
		channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

		//绑定普通交换机和普通队列
		channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_ROUTING_KEY);
		//绑定死信交换机和死信队列
		channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);

		//接收消息
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println(new String(message.getBody()));
//			if (message.getEnvelope().getDeliveryTag() == 5) {
//				System.out.println("拒绝消息:" + new String(message.getBody()));
//				channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
//			} else {
//				System.out.println("接收消息:" + new String(message.getBody()));
//				channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
//			}
		};

		//消费者取消消费时回调接口
		CancelCallback cancelCallback = consumerTag -> {
			System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
		};

		channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, cancelCallback);
	}
}