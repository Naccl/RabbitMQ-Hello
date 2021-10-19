package top.naccl.rabbitmq.fanout;

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
public class ReceiveLogs02 {
	public static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();
		//声明一个队列
		/**
		 * 生成一个临时队列，队列的名称是随机的
		 * 当消费者断开与队列的连接时，队列自动删除
		 */
		String queueName = channel.queueDeclare().getQueue();
		//绑定交换机与队列
		channel.queueBind(queueName, EXCHANGE_NAME, "2");
		System.out.println("等待接收消息，把接收到的消息打印在屏幕上......");

		//接收消息
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println("ReceiveLogs02 打印消息:" + new String(message.getBody()));
		};

		//消费者取消消费时回调接口
		CancelCallback cancelCallback = consumerTag -> {
			System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
		};

		channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
	}
}
