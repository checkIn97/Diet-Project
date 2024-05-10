package com.demo.persistenceTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.persistence.HistoryRepository;
import com.demo.persistence.UsersInOutRepository;

@SpringBootTest
public class HistoryRepositoryTest {
	@Autowired
	private HistoryRepository historyRepo;
	
	@Autowired
	private UsersInOutRepository usersInOutRepo;
	
	@Disabled
	@Test
	public void getHistoryListNotServedYet() {
		Users user = usersInOutRepo.findById(100).get();
		List<History> historyList = historyRepo.getHistoryListNotConfirmedYet(user);
		
		for (History history : historyList) {
			System.out.println(history.getFood().getName());
		}
		
	}
	
	@Disabled
	@Test
	public void getHistoryListServed() {
		Users user = usersInOutRepo.findById(100).get();
		List<History> historyList = historyRepo.getHistoryListConfirmed(user);
		
		for (History history : historyList) {
			System.out.println(history.getFood().getName());
		}
		
	}
	
	@Disabled
	@Test
	public void totalKcalToday() {
		Users user = usersInOutRepo.findById(100).get();
		List<History> historyList = historyRepo.getHistoryListConfirmed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalKcalToday = 0f;
		for (History history : historyList) {
			if (String.valueOf(history.getServedDate()).substring(0, 10).equals(today))
				totalKcalToday += history.getFood().getFoodDetail().getKcal();
		}
		System.out.println(totalKcalToday);
	}
	
}
