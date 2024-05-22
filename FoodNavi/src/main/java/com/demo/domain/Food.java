package com.demo.domain;

import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Food {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int fseq;
	
	private String name;	
	private String img;

	@ToString.Exclude
	@OneToOne(mappedBy="food", fetch=FetchType.EAGER)
	private FoodDetail foodDetail;

	@ToString.Exclude
	@OneToMany(mappedBy="food", fetch=FetchType.LAZY)
	private List<FoodIngredient> foodIngredientList;
	
}
