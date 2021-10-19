package top.naccl.rabbitmqspringboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2021-10-19
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {
	@GetMapping("/sendMsg/{message}")
	public void sendMsg(@PathVariable String message) {
		log.info("当前时间:{}, 发送一条消息给两个TTL队列:{}", LocalDateTime.now(), message);
	}
}
