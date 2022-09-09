package com.example.vuetest.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MainUser implements Serializable {
    int id;
    String username;
    String password;
    String role;

    Role newrole;
    //Authority authority;
}
