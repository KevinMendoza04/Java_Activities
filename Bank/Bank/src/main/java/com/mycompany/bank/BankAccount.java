/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.bank;

import javax.swing.JOptionPane;

/**
 *
 * @author kem
 */
public class BankAccount {

    private String accountNumber;
    private String nameAH;
    private double balance;
    private boolean active = true;

    public BankAccount(String accountNumber, String nameAH, double balance) {
        this.accountNumber = accountNumber;
        this.nameAH = nameAH;
        this.balance = balance;
        this.active = true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return nameAH;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public boolean deposit(double amount) {
        if (active && amount > 0) {
            balance += amount;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean withdraw(double amount){
        if (active && amount > 0 && balance >= amount){
            balance -= amount;
            return true;
        } else {
            return false;
        }
       
    }
    
    public String getBalanceType(){
        if (balance < 100000){
            return "low balance";
        } else if (balance >= 100000 && balance <= 1000000){
            return "stable balance";
        } else {
            return "high balance";
        }
        
    }
    
    
    public String getAccountInfo(){
        String status;
        if (active){
            status = "Active.";
        } else {
            status = "Inactive.";
        }
        return """
               
               ==============BANK ACCOUNT=============
               Account Number: """ + accountNumber + "\nAccount's holder name: " + nameAH + "\nBalance: " + String.format("%,.0f", balance) + "\nAccount status: " + status + "\nClasification: " + getBalanceType();  
                    
    }
    
    public void deactivateAccount(){
        active = false;
        
    }
}

