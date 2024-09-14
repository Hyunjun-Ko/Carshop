package com.packt.cardatabase.domain;

// jwt에서 인증을 위한 자격 증명을 포함할 POJO 클래스
// 자격 증명을 데이터베이스에 저장하지는 않으므로 이 클래스에는 @Entity
// 어노테이션을 지정하지 않는다
public class AccountCredentials {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
