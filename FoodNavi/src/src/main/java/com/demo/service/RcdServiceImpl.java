package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.domain.Rcd;
import com.demo.domain.Users;
import com.demo.persistence.RcdRepository;

@Service
public class RcdServiceImpl implements RcdService {
	
	@Autowired
	private RcdRepository rcdRepo;
	
	@Override
	public void rcdUpdate(Users user, Food food) {
		Rcd rcd = rcdRepo.findByUserAndFood(user, food);
		if (rcd == null) {
			rcd = new Rcd();
			rcd.setUser(user);
			rcd.setFood(food);
			rcdRepo.save(rcd);
		} else {
			rcdRepo.delete(rcd);
		}
	}
}
