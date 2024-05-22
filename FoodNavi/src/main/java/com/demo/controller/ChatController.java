package com.demo.controller;

import java.io.IOException;
import java.util.List;

import com.demo.domain.Food;
import com.demo.dto.ChatMessage;
import com.demo.service.FoodScanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

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

	@Autowired
	public FoodScanService foodScanService;

	private final SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/chat/message")
	public void message(ChatMessage message){
		if (ChatMessage.MessageType.ENTER.equals(message.getType()))
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		if (ChatMessage.MessageType.BOT.equals(message.getType())){
			String result = searchFood(message.getMessage());
			if (!result.equals(message.getMessage())){
				message.setMessage(result
						+"의 주문페이지를 보여드릴게요.\n https://www.coupang.com/np/search?component=&q="+ result +"&channel=user");
				message.setSender("[챗봇 상담사]");
			} else {
				message.setMessage("'" + result + "'"
						+"의 요청을 처리하지 못했습니다.\n (찾으시는 음식이 데이터에 존재하지 않습니다.)");
				message.setSender("[챗봇 상담사]");
			}
		}

		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(),message);
	}

	public String searchFood(String message){
		List<Food> foodList = foodScanService.getFoodSearchList();
		String result = "";
		for(Food foodName : foodList){
			if(message.contains(foodName.getName())){
				result = foodName.getName();
				break;
			} else {
				result = message;
			}
		}

		return result;
	}


}
