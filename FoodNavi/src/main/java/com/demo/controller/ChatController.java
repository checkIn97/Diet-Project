package com.demo.controller;

import java.util.List;

import com.demo.dto.ChatMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.ChatRoom;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
public class ChatController {
//
//	@RequestMapping("/chat")
//	public String chat() {
//		
//		return "chat";
//		
//	}

	private final SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/chat/message")
	public void message(ChatMessage message){
		if (ChatMessage.MessageType.ENTER.equals(message.getType()))
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");

		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(),message);
	}
	
}
