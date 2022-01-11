/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package data.models;

/**
 *
 * @author hasan
 */
public interface IEntrance {
    Trader signUp(String email, String password);
    Trader signIn(String email, String password);
    void logOut();
    boolean checkIfEmailExist(String IEmail);
}
