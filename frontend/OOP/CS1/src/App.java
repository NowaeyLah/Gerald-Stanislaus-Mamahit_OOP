public class App {
    public class player {
        int hp;
        int power;
        int spellCards;
        String playerName;

        public player (int hp, int power, int spellCards, String playerName) {
            this.hp = hp;
            this.power = power;
            this.spellCards = spellCards;
            this.playerName = playerName;
        }

            public void takeDamage(int damage) {
        // 1. Kurangi hp sebesar nilai damage.
        hp=hp-damage;
        // 2. HP tidak boleh bernilai negatif.
        if (hp<0){
            hp = 0;
        } else {
            System.out.println(playerName + " took " + damage + " damage! Remaining HP: " + hp);
        }
        // 3. Jika HP masih lebih dari 0, tampilkan HP yang tersisa dalam format: [PlayerName] took [damage] damage! Remaining HP: [hp]

        // 4. Jika HP menjadi 0, tampilkan pesan bahwa Player telah dikalahkan.
        if (hp==0){
            System.out.println("Player telah dikalahkan.");
        }

        public void shoot(enemy target) {
        // 1. Buat int bernama damage yang dihitung dengan menambahkan power sebanyak 10.
            int damage;
            damage = power + 10;
        // 2. Tampilkan informasi bahwa Player menembak Enemy dalam format: [name] shoots [TargetName] dealing [damage] DMG!
            System.out.println(playerName + " shoots "+ enemy + " dealing "+ damage+ " DMG!");
        // 3. Panggil method takeDamage() milik object Enemy.
        target.takeDamage(damage);
    }

    }
    }

    public class enemy{
        int hp;
        int maxHp;
        String enemyName;

        public void player (int hp, String enemyName) {
            this.hp = hp;
            this.enemyName = enemyName;
        }

            public void takeDamage(int damage) {
        // 1. Kurangi hp sebesar nilai damage.
        hp=hp-damage;
        // 2. HP tidak boleh bernilai negatif.
        if (hp<0){
            hp = 0;
        } else {
            System.out.println(enemyName + " took " + damage + " damage! Remaining HP: " + hp);
        }
        // 3. Jika HP masih lebih dari 0, tampilkan HP yang tersisa dalam format: [PlayerName] took [damage] damage! Remaining HP: [hp]

        // 4. Jika HP menjadi 0, tampilkan pesan bahwa Player telah dikalahkan.
        if (hp==0){
            System.out.println("Musuh telah dikalahkan.");
        }

        public void attack(player player, int damage) {
        // 1. Tampilkan informasi bahwa Enemy menyerang Player dalam format: [EnemyName] unleashes bullet barrage on [PlayerName]!
        
        // 2. Panggil takeDamage() milik Player menggunakan damage yang diberikan.
    }

    }



    }
}
