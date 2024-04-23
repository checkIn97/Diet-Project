package com.demo.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Users {
	@Column(length=50)
	private String userid;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int useq;
	
	@Column(length=100)
	private String userpw;
	private String name;
	
	@Column(length=1)
	private String sex;	
	
	private int age;
	private int height;
	private int weight;
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Board> boardList = new ArrayList<>();
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Comments> commentList = new ArrayList<>();
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<Rcd> likeList = new ArrayList<>();
	
	
}
