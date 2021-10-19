package top.naccl.rabbitmq.ack;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-03
 */
public class Worker04 {
	public static final String TASK_QUEUE_NAME = "ack_queue";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();
		System.out.println("C2等待接收消息，处理时间短");

		DeliverCallback deliverCallback = (consumerTag, message) -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("接收到消息:" + new String(message.getBody()));

			//手动应答
			channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
		};

		CancelCallback cancelCallback = consumerTag -> {
			System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
		};


		//设置不公平分发
//		channel.basicQos(1);
		//设置手动应答
		boolean autoAck = false;

		channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
	}
}
