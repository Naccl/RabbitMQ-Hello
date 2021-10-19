package top.naccl.rabbitmq.pollpattren;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;

/**
 * @Description: 消费者01
 * @Author: Naccl
 * @Date: 2021-09-28
 */
public class Worker01 {
	public static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException {
		//获取信道
		Channel channel = RabbitMqUtils.getChannel();

		//消息接收
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println("接收到的消息：" + new String(message.getBody()));
		};

		//消息接收被取消时，执行下面的内容
		CancelCallback cancelCallback = consumerTag -> {
			System.out.println("消费者取消消费接口回调逻辑");
		};

		/**
		 * 消费者消费消息
		 * @param queue 消费哪个队列
		 * @param autoAck 自动应答为true，手动应答为false
		 * @param deliverCallback 消费消息时回调
		 * @param cancelCallback 消费者取消消费时回调
		 */
		channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
	}
}
