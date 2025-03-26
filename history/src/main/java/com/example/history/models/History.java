package com.example.history.models;

import java.util.Date;

import java.util.Map;


import org.hibernate.annotations.JdbcTypeCode;

import org.hibernate.type.SqlTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "history")
public class History {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long stockId;
    
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Date, Double> history;
}
