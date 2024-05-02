package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class FoodTable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int tseq;
	
	@ManyToOne
	@JoinColumn(name="useq", nullable=false)
	private Users user;
	
	@ManyToOne
	@JoinColumn(name="fseq", nullable=false)
	private Food food;
	
	private int serveNumber;
	
	private String mealType;
	
	private Date servedDate;
}
