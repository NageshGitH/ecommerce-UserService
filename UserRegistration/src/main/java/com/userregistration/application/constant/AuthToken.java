package com.userregistration.application.constant;

public class AuthToken 
{

    private String token;
    private String username;
    private String msg;

    public AuthToken(){

    }

    public AuthToken(String token, String username,String msg){
        this.token = token;
        this.username = username;
        this.msg=msg;
    }

    public AuthToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
    
    
}
