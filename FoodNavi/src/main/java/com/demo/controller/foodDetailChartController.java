package com.demo.controller;

import com.demo.dto.FoodVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foodDetailChart/data")
public class foodDetailChartController {
    @GetMapping("/drawChart")
    public float[] foodDetailChart(HttpSession session) {
        FoodVo vo = (FoodVo) session.getAttribute("foodVo");
        float carb = vo.getFood().getFoodDetail().getCarb();
        float prt = vo.getFood().getFoodDetail().getPrt();
        float fat = vo.getFood().getFoodDetail().getFat();
        float[] irgd = new float[4];
        irgd[0] = carb;
        irgd[1] = prt;
        irgd[2] = fat;
        return irgd;
    }
}
