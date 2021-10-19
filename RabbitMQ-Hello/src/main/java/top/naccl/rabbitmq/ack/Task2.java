package top.naccl.rabbitmq.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-03
 */
public class Task2 {

	public static final String TASK_QUEUE_NAME = "ack_queue";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();

		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String msg = scanner.next();
			channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
			System.out.println("发出消息" + msg);
		}
	}
}
