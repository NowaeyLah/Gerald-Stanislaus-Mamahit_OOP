package com.gerald.frontend;

public class Test {
    public static void main(String[] args) {
        System.out.println("=== TOUHOU OOP PRACTICUM - MODULE 1: BASIC CLASSES & OBJECT INTERACTION ===");

        Player reimu = new Player("Reimu Hakurei", 100, 15, 3);
        Enemy fairyBoss = new Enemy("Cirno (Stage 2 Boss)", 50);

        System.out.println("\n--- Initial Battle State ---");
        System.out.println("Player: " + reimu.getName() + " | HP: 100 | Power: 15 | SpellCards: 3");
        System.out.println("Enemy:  " + fairyBoss.getName() + " | HP: 50");

        System.out.println("\n--- Turn 1: Player Shoots Enemy ---");
        reimu.shoot(fairyBoss);

        System.out.println("\n--- Turn 2: Enemy Counter-attacks ---");
        fairyBoss.attack(reimu, 30);

        System.out.println("\n--- Turn 3: Player Shoots Enemy Finishing Blow ---");
        reimu.shoot(fairyBoss);

        System.out.println("\n--- Turn 4: Enemy Deals Fatal Damage to Reimu ---");
        fairyBoss.attack(reimu, 80);

        System.out.println("\n=== Battle Simulation Complete ===");
    }
}
