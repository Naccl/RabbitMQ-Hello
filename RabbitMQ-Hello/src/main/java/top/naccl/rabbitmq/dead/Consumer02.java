package top.naccl.rabbitmq.dead;

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
public class Consumer02 {
	//死信队列名称
	public static final String DEAD_QUEUE = "dead_queue";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();

		//接收消息
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println(new String(message.getBody()));
		};

		//消费者取消消费时回调接口
		CancelCallback cancelCallback = consumerTag -> {
			System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
		};

		channel.basicConsume(DEAD_QUEUE, true, deliverCallback, cancelCallback);
	}
}