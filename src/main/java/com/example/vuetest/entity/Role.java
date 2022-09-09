package com.example.vuetest.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Role implements Serializable {
    private String roleID;
    private String roleName;

    public String getRoleName(){
        return "ROLE_"+roleName;
    }
}
