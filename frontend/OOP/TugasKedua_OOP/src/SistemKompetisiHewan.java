import java.util.ArrayList;
import java.util.List;

abstract class Animal {
    protected String famili;
    protected String genus;
    protected String species;

    public Animal(String famili, String genus, String species) {
        this.famili = famili;
        this.genus = genus;
        this.species = species;
    }
    
    public abstract String mulaiKompetisi();
}

class Anjing extends Animal {
    private String pintar;

    public Anjing(String famili, String genus, String species, String pintar) {
        super(famili, genus, species);
        this.pintar = pintar;
    }

    @Override
    public String mulaiKompetisi() {
        return "Juri mulai menilai " + this.famili + " " + this.genus + " " + this.species + " dengan tingkat kepintaran " + this.pintar + ".";
    }
}

class Kucing extends Animal {
    private String lucu;

    public Kucing(String famili, String genus, String species, String lucu) {
        super(famili, genus, species);
        this.lucu = lucu;
    }

    @Override
    public String mulaiKompetisi() {
        return "Juri mulai menilai " + this.famili + " " + this.genus + " " + this.species + " dengan tingkat kelucuan " + this.lucu + ".";
    }
}

class Kompetisi {
    private String namaAcara;
    private List<Animal> peserta;

    public Kompetisi(String namaAcara) {
        this.namaAcara = namaAcara;
        this.peserta = new ArrayList<>();
    }

    public void daftarPeserta(Animal hewan) {
        peserta.add(hewan);
        System.out.println("Peserta baru terdaftar: " + hewan.species);
    }

    public void mulaiPenilaian() {
        System.out.println("\n--- Memulai " + namaAcara + " ---");
        for (Animal hewan : peserta) {
            System.out.println(hewan.mulaiKompetisi());
        }
    }
}

public class SistemKompetisiHewan {
    public static void main(String[] args) {
        Kompetisi showNasional = new Kompetisi("National Pet Show 2026");

        Anjing goldenRetriever = new Anjing("Canidae", "Canis", "C. familiaris", "Tinggi");
        Kucing persia = new Kucing("Felidae", "Felis", "F. catus", "Sangat Lucu");

        showNasional.daftarPeserta(goldenRetriever);
        showNasional.daftarPeserta(persia);

        showNasional.mulaiPenilaian();
    }
}