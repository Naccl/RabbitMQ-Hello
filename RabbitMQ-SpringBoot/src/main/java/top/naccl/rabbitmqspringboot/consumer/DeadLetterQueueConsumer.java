package top.naccl.rabbitmqspringboot.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.naccl.rabbitmqspringboot.config.TtlQueueConfig;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-20
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {
	@RabbitListener(queues = TtlQueueConfig.DEAD_LETTER_QUEUE_D)
	public void receiveD(Message message, Channel channel) {
		String msg = new String(message.getBody());
		log.info("当前时间:{}, 收到死信队列的消息:{}", LocalDateTime.now(), msg);
	}
}
