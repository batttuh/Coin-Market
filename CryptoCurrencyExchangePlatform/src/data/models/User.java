package data.models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hasan
 */
abstract class User {
    private String email = "email";
    private String userId = "userId";
    public User(String email, String userId) {
        this.email = email;
        this.userId = userId;
    }
    
    public String getUserEmail(){
        return this.email;
    }
    public String getUserId(){
        return userId;
    }
}
