package com.demo.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class FoodDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int fdseq;
	
	private float kcal;
	private float fat;
	private float carb;
	private float prt;
	
	private String tasteType;	
	private String nationType;	
	private String healthyType;	
	private int veganType;
	
	@OneToOne
	@JoinColumn(name="fseq", nullable=false)
	private Food food;
	
}
