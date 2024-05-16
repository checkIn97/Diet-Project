package com.demo.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.domain.Exercise;
import com.demo.domain.ExerciseOption;
import com.demo.domain.Users;
import com.demo.persistence.ExerciseOptionRepository;
import com.demo.persistence.ExerciseRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@RestController
public class ExerciseOptionController {

    @Autowired
    private ExerciseOptionRepository exerciseOptionRepository;
    @Autowired
    private ExerciseRepository exerciseRepo;

    @GetMapping("/activities/search")
    public List<String> searchIngredients(@RequestParam("term") String term) {
        return exerciseOptionRepository.findByTypeContainingIgnoreCase(term)
                .stream()
                .map(ExerciseOption::getType)
                .collect(Collectors.toList());
    }
    
    @PostMapping("/exercise_record")
    public void insertRecord(@RequestParam("activityType") String activityType,
    						 @RequestParam("activityTime") String activityTime,
    						 HttpSession session, HttpServletResponse response) throws IOException {
    	Users user = (Users)session.getAttribute("loginUser");
    	Optional<ExerciseOption> exo = exerciseOptionRepository.findByType(activityType);
    	if (!exo.isEmpty()) {
	    	Exercise vo = Exercise.builder()
	    			.exerciseDate(new Date())
	    			.exerciseOption(exo.get())
	    			.exerciseType(activityType)
	    			.time(Integer.parseInt(activityTime))
	    			.user(user)
	    			.build();
	    	exerciseRepo.save(vo);
    	} else {
    		System.out.println("없는 운동");
    	}
    	String redirect_uri="http://localhost:8080/user_myactivity_view";
    	response.sendRedirect(redirect_uri);
    }
}