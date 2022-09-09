package com.example.vuetest.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Books implements Serializable {
    int id;
    String bookname;
    String author;
    Integer borrowedby;
}
