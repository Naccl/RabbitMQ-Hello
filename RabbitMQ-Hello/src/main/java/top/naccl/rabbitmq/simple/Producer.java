package top.naccl.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;

/**
 * @Description: 生产者
 * @Author: Naccl
 * @Date: 2021-09-28
 */
public class Producer {
	public static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException {
		//创建信道
		Channel channel = RabbitMqUtils.getChannel();
		//声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//发送消息
		String msg = "Hello!";
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
	}

}
