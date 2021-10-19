package top.naccl.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Description: 工具类
 * @Author: Naccl
 * @Date: 2021-09-28
 */
public class RabbitMqUtils {
	private static ConnectionFactory factory = new ConnectionFactory();
	private static Connection connection;

	static {
		factory.setHost("192.168.1.1");
		factory.setUsername("Naccl");
		factory.setPassword("123456");
		//建立连接
		try {
			connection = factory.newConnection();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static Channel getChannel() throws IOException {
		//创建信道
		return connection.createChannel();
	}
}
