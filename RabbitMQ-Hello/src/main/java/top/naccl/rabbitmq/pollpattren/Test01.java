package top.naccl.rabbitmq.pollpattren;

import com.rabbitmq.client.Channel;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-09-28
 */
public class Test01 {
	//队列名称
	public static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException {
		//获取信道
		Channel channel = RabbitMqUtils.getChannel();

		/**
		 * 声明队列
		 * @param queue 队列名称
		 * @param durable 队列中的消息是否持久化，默认情况消息存储在内存中
		 * @param exclusive 是否排他队列
		 * @param autoDelete 是否自动删除队列，最后一个消费者断开连接后自动删除队列
		 * @param arguments 其它参数
		 */
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		//从控制台接收参数
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String msg = scanner.next();

			/**
			 * 发送消息
			 * @param exchange 发送到哪个交换机
			 * @param routingKey 路由的key，队列名称
			 * @param props 其它参数
			 * @param body 消息体
			 */
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			System.out.println("发送消息完成：" + msg);
		}
	}
}
