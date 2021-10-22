package top.naccl.rabbitmq.priority;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 优先级队列
 * @Author: Naccl
 * @Date: 2021-10-22
 */
public class Producer {
	public static final String QUEUE_NAME = "priority";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();
		Map<String, Object> arguments = new HashMap<>();
		//允许范围0-255，现在是0-10，设置过大浪费CPU与内存
		arguments.put("x-max-priority", 10);
		channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

		for (int i = 0; i < 10; i++) {
			String msg = i + "";
			if (i == 5) {
				AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
				channel.basicPublish("", QUEUE_NAME, properties, msg.getBytes());
			} else {
				channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			}
		}
		System.out.println("消息发送完毕");
	}
}
