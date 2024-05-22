package com.demo.domain;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
public class UserChange {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int ucseq;
	
	@ManyToOne
	@JoinColumn(name="useq", nullable=false)
	private Users user;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@ColumnDefault("sysdate")
	@Column(updatable=false)
	private Date createdAt;
	
	private float height;
	private float weight;
	private int age;
	private String sex;	
	
}