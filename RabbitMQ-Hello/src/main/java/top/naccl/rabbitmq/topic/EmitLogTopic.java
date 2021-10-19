package top.naccl.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-17
 */
public class EmitLogTopic {
	public static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws IOException {
		Channel channel = RabbitMqUtils.getChannel();
		Map<String, String> bindingKeyMap = new LinkedHashMap<>();
		bindingKeyMap.put("quick.orange.rabbit", "被队列 Q1 Q2 接收到");
		bindingKeyMap.put("lazy.orange.elephant", "被队列 Q1 Q2 接收到");
		bindingKeyMap.put("quick.orange.fox", "被队列 Q1 接收到");
		bindingKeyMap.put("lazy.brown.fox", "被队列 Q2 接收到");
		bindingKeyMap.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列 Q2 接收一次");
		bindingKeyMap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
		bindingKeyMap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");
		bindingKeyMap.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");

		bindingKeyMap.forEach((k, v) -> {
			try {
				channel.basicPublish(EXCHANGE_NAME, k, null, v.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(k + v);
		});

	}
}
