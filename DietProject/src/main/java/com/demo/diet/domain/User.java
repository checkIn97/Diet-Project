package com.demo.diet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id; // User 테이블 키값

    @Column(nullable = false, length = 50) private String userid; // 로그인 아이디
    @Column(nullable = false, length = 100) private String userpw; // 로그인 비밀번호
    @Column(nullable = false) private String name; // 회원 이름
    @Column(nullable = false, length = 1) private String sex; // 회원 성별(1글자로 표현 여자: W / 남자: M)
    @Column(nullable = false) private int age; // 회원 나이
    @Column(nullable = false) private int height; // 회원 키
    @Column(nullable = false) private int weight; // 회원 몸무게

}
