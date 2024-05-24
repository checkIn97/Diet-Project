package com.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
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
				if (result.equals("커뮤니티")){
					String cate = "/board_list";
					message.setMessage("커뮤니티 으로 이동하시겠습니까?");
					message.setCate(cate);
				} else if (result.equals("나의변화")) {
					String cate = "/user_mychange_view";
					message.setMessage("나의변화 으로 이동하시겠습니까?");
					message.setCate(cate);
				} else if (result.equals("나의활동")) {
					String cate = "/user_myactivity_view";
					message.setMessage("나의활동 으로 이동하시겠습니까?");
					message.setCate(cate);
				} else if (result.equals("마이페이지")) {
					String cate = "/pw_check";
					message.setMessage("마이페이지 으로 이동하시겠습니까?");
					message.setCate(cate);
				} else if (result.equals("식단추천")){
					String cate = "/foodRecommendation";
					message.setMessage("식단추천 으로 이동하시겠습니까?");
					message.setCate(cate);
				} else {
					String link = "https://m.coupang.com/nm/search?q=" + result;
					message.setMessage(result + "의 주문페이지를 보여드릴게요.\n");
					message.setLink(link);
				}
			} else {
				message.setMessage("'" + result + "'"
						+"의 요청을 처리하지 못했습니다.\n (데이터에 존재하지 않거나 처리할 수 없는 요청입니다.)");
			}
			message.setSender("[챗봇 상담사]");
		}
		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(),message);
	}

	public String searchFood(String message){
		List<Food> foodList = foodScanService.getFoodSearchList();
		List<String> categoryList = new ArrayList<>();
		categoryList.add("커뮤니티");
		categoryList.add("나의변화");
		categoryList.add("나의활동");
		categoryList.add("마이페이지");
		categoryList.add("식단추천");
		String result = "";
		for(Food foodName : foodList){
			if(message.replaceAll(" ", "").contains(foodName.getName().replaceAll(" ", ""))){
				result = "'" + foodName.getName() + "'";
				break;
			} else {
				for (String category : categoryList) {
					if(message.replaceAll(" ", "").contains(category)){
						result = category;
						break;
					} else {
						result = message;
					}
				}
			}
		}
		return result;
	}


}
