package top.naccl.rabbitmq.direct;

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
public class ReceiveLogsDirect02 {
	public static final String EXCHANGE_NAME = "direct_logs";
	public static final String QUEUE_NAME = "disk";
	public static final String ROUTING_KEY_ERROR = "error";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();
		//声明一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//绑定一个routingKey
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_ERROR);

		//接收消息
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println("ReceiveLogsDirect02 打印消息:" + new String(message.getBody()));
		};

		//消费者取消消费时回调接口
		CancelCallback cancelCallback = consumerTag -> {
			System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
		};

		channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
	}
}
