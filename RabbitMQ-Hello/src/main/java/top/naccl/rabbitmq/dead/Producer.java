package top.naccl.rabbitmq.dead;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-17
 */
public class Producer {
	//普通交换机名称
	public static final String NORMAL_EXCHANGE = "normal_exchange";
	//普通队列routingKey
	public static final String NORMAL_ROUTING_KEY = "normal_routing_key";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();

		//死信消息 设置TTL时间
		AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().expiration("10000").build();

		for (int i = 0; i < 10; i++) {
			String msg = i + "";
			channel.basicPublish(NORMAL_EXCHANGE, NORMAL_ROUTING_KEY, basicProperties, msg.getBytes());
			System.out.println("生产者发出消息：" + msg);
		}
	}
}