package top.naccl.rabbitmq.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-17
 */
public class EmitLog {
	public static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();
		//声明一个交换机
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String msg = scanner.next();
			channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
			System.out.println("生产者发出消息：" + msg);
		}
	}
}
