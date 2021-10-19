package top.naccl.rabbitmq.publishconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import top.naccl.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Description: 发布确认模式
 * 1.单个确认
 * 2.批量确认
 * 3.异步批量确认
 * @Author: Naccl
 * @Date: 2021-10-04
 */
public class ConfirmMessage {

	public static final int MESSAGE_COUNT = 1000;

	public static void main(String[] args) throws IOException, InterruptedException {
//		publishMessageSignal();
//		publishMessageBatch();
		publishMessageAsync();
	}

	public static void publishMessageSignal() throws IOException, InterruptedException {
		Channel channel = RabbitMqUtils.getChannel();
		String queueName = UUID.randomUUID().toString();
		channel.queueDeclare(queueName, true, false, false, null);
		//开启发布确认
		channel.confirmSelect();

		long begin = System.currentTimeMillis();

		for (int i = 0; i < MESSAGE_COUNT; i++) {
			String msg = i + "";
			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
			//单个消息就马上进行发布确认
			boolean flag = channel.waitForConfirms();
			if (flag) {
				System.out.println("消息发送成功" + i);
			}
		}

		long end = System.currentTimeMillis();
		System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
	}

	public static void publishMessageBatch() throws IOException, InterruptedException {
		Channel channel = RabbitMqUtils.getChannel();
		String queueName = UUID.randomUUID().toString();
		channel.queueDeclare(queueName, true, false, false, null);
		//开启发布确认
		channel.confirmSelect();

		long begin = System.currentTimeMillis();

		//批量确认阈值
		int batchSize = 100;
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			String msg = i + "";
			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
			//批量确认消息
			if ((i + 1) % batchSize == 0) {
				channel.waitForConfirms();
				System.out.println("消息发送成功" + i);
			}
		}

		long end = System.currentTimeMillis();
		System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
	}

	public static void publishMessageAsync() throws IOException {
		Channel channel = RabbitMqUtils.getChannel();
		String queueName = UUID.randomUUID().toString();
		channel.queueDeclare(queueName, true, false, false, null);
		//开启发布确认
		channel.confirmSelect();

		/**
		 * 线程安全有序的一个hash表 适用于高并发的情况下
		 * 1.轻松地将序号与消息进行关联
		 * 2.轻松批量删除条目 只要给到序号
		 * 3.支持高并发（多线程）
		 */
		ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

		//消息确认成功 回调函数
		ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
			//2.删除掉已经确认的消息 剩下的就是未确认的消息
			if (multiple) {
				ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
				confirmed.clear();
			} else {
				outstandingConfirms.remove(deliveryTag);
			}

			System.out.println("确认的消息：" + deliveryTag);
		};

		//消息确认失败 回调函数
		ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
			//3.打印一下未确认的消息都有哪些
			String msg = outstandingConfirms.get(deliveryTag);

			System.out.println("未确认的消息：" + msg + ", tag:" + deliveryTag);
		};

		channel.addConfirmListener(ackCallback, nackCallback);

		long begin = System.currentTimeMillis();

		//批量发送消息
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			String msg = i + "";

			//1.此处记录下所有要发送的消息
			outstandingConfirms.put(channel.getNextPublishSeqNo(), msg);

			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

			System.out.println("消息发送成功" + i);
		}

		long end = System.currentTimeMillis();
		System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时" + (end - begin) + "ms");
	}
}
