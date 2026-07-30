package com.mycompany.videogame;

import javax.swing.JOptionPane;

/**
 *
 * @author kem
 */
public class VideoGame {

    private String name;
    private String developer;
    private double price;
    private int timeGame;
    private char clasification;
    private boolean multiplayer;

    public void getData() {
        name = JOptionPane.showInputDialog("Enter your name:");
        developer = JOptionPane.showInputDialog("Enter developer name: ");
        price = Double.parseDouble(JOptionPane.showInputDialog("Enter the price: "));
        timeGame = Integer.parseInt(JOptionPane.showInputDialog("How many hours it take to complete the game: "));
        clasification = JOptionPane.showInputDialog("Enter clasification type: ").charAt(0);
        multiplayer = Boolean.parseBoolean(JOptionPane.showInputDialog("Is this a multiplayer game?: "));

    }

    public void showInformation() {
        String duration;
        if (timeGame <= 20) {
            duration = "Short Duration.";
        } else if (timeGame > 20 && timeGame <= 50) {
            duration = "Medium Duration.";
        } else {
            duration = "Long Duration.";
        }

        String priceRange;
        if (price <= 100000) {
            priceRange = "Cheap Game.";
        } else if (price > 100000 && price <= 200000) {
            priceRange = "Medium Price.";
        } else {
            priceRange = "Premium Game.";
        }
       
        String playersMode; 
        if (multiplayer){
            playersMode = "Multiplayer.";
        } else {
            playersMode = "Single player.";
        }
        JOptionPane.showMessageDialog(null,"========== VIDEOGAME ==========\n" + "Name: " + name + "\nDeveloper: " + developer + "\nPrice: " + price + "\nGame Time: " + timeGame + "\nClasification: " + clasification + "\nMultiplayer: " + playersMode + "\nPrice Range: " + priceRange + "\nDuration: " + duration);

    }
}
