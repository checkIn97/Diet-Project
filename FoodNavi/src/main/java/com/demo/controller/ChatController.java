package com.demo.controller;

import java.io.IOException;
import java.util.List;

import com.demo.domain.Food;
import com.demo.dto.ChatMessage;
import com.demo.service.FoodScanService;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


	private final FoodScanService foodScanService;
	private final SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/chat/message")
	public void handleMessage(ChatMessage message){
		if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		} else if (ChatMessage.MessageType.BOT.equals(message.getType())){
			String result = searchFood(message.getMessage());
			if (!result.equals(message.getMessage())){
				String link = "https://m.coupang.com/nm/search?q="+ result;
				message.setMessage(result
						+"의 주문페이지를 보여드릴게요.\n");
				message.setSender("[챗봇 상담사]");
				message.setLink(link);
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
				result = "'" + foodName.getName() + "'";
				break;
			} else {
				result = message;
			}
		}

		return result;
	}


}
