package top.naccl.rabbitmq.simple;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;

/**
 * @Description: 消费者
 * @Author: Naccl
 * @Date: 2021-09-28
 */
public class Consumer {
	public static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException {
		//创建信道
		Channel channel = RabbitMqUtils.getChannel();

		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println(new String(message.getBody()));
		};

		CancelCallback cancelCallback = (consumerTag) -> {
			System.out.println("消费消息被中断");
		};

		channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
	}
}
