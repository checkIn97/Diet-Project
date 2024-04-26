package com.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FoodRecomController {

    @GetMapping("/foodRecommendation")
    public String dietRecommendation() {
        return "food/foodDay";
    }
}
