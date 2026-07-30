package com.mycompany.bank;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        String accountNumber = JOptionPane.showInputDialog("Enter your account number: ");
        String nameAH = JOptionPane.showInputDialog("Enter account's holder name: ");
        double balance = Double.parseDouble(JOptionPane.showInputDialog("Enter initial balance: "));
        BankAccount account = new BankAccount(accountNumber, nameAH, balance);

        int option;
        do {
            String menuText = """
                              ======== RiWi BANK ========
                              
                              1. Check account.
                              2. Deposit.
                              3. Withdraw.
                              4. Balance.
                              5. Deactivate account.
                              6. Exit.""";
            String input = JOptionPane.showInputDialog(menuText);
            option = Integer.parseInt(input);
            switch (option) {
                case 1 -> JOptionPane.showMessageDialog(null, account.getAccountInfo());
                case 2 -> {
                    double depositAmount = Double.parseDouble(JOptionPane.showInputDialog("Amount to deposit:"));
                    boolean depositResult = account.deposit(depositAmount);
                    if (depositResult) {
                        JOptionPane.showMessageDialog(null, "Deposit successful.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Unable to deposit.");
                    }
                }
                case 3 -> {
                    double withdrawAmount = Double.parseDouble(JOptionPane.showInputDialog("Amount to withdraw:"));
                    boolean withdrawResult = account.withdraw(withdrawAmount);
                    if (withdrawResult) {
                        JOptionPane.showMessageDialog(null, "Withdrawal successful.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Unable to withdraw.");
                    }
                }
                case 4 -> JOptionPane.showMessageDialog(null, "\nBalance: " + String.format("%,.0f", balance) + "\nType: " + account.getBalanceType());
                case 5 -> {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to deactivate the account?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        account.deactivateAccount();
                    }
                }
                case 6 -> JOptionPane.showMessageDialog(null, "Thanks for using RiWi Bank.");
                default -> JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        } while (option != 6);
    }
}
