package com.danaga.memberResponse;

import java.util.HashMap;

import lombok.Data;

@Data
public class MemberResponse {

    private int status;
    private String message;
    private Object data;

    public MemberResponse() {
        this.status = 0;
        this.data =new HashMap();
        this.message = "";
    }
    
}