package com.anteprj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "push")
public class Push {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    public static Push create(String token) {
        return Push.builder()
                .token(token)
                .createdDate(LocalDateTime.now())
                .build();
    }

    public void update(String token) {
        this.token = token;
        this.createdDate = LocalDateTime.now();
    }
}
