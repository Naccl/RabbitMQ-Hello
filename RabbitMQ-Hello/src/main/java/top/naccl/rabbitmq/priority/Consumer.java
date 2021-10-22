package top.naccl.rabbitmq.priority;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;

/**
 * @Description: 消费者
 * @Author: Naccl
 * @Date: 2021-10-22
 */
public class Consumer {
	public static final String QUEUE_NAME = "priority";

	public static void main(String[] args) throws IOException {
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
