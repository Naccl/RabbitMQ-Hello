package top.naccl.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-17
 */
public class ReceiveLogsTopic02 {
	public static final String EXCHANGE_NAME = "topic_logs";
	public static final String QUEUE_NAME = "Q2";
	public static final String ROUTING_KEY1 = "*.*.rabbit";
	public static final String ROUTING_KEY2 = "lazy.#";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();
		//声明一个交换机
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
		//声明一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//绑定一个routingKey
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY1);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY2);
		System.out.println("等待接收消息......");

		//接收消息
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println("queue:" + QUEUE_NAME + ", routingKey:" + message.getEnvelope().getRoutingKey() + ", msg:" + new String(message.getBody()));
		};

		//消费者取消消费时回调接口
		CancelCallback cancelCallback = consumerTag -> {
			System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
		};

		channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
	}
}
