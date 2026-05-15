package hust.soict.dsai.aims.media;

public class DigitalVideoDisc extends Disc implements Playable {
    private static int nbDigitalVideoDiscs = 0;

    public DigitalVideoDisc(String title) {
        super(title);
        nbDigitalVideoDiscs++;
        this.setId(nbDigitalVideoDiscs);
    }

    public DigitalVideoDisc(String title, String category, float cost) {
        super(title, category, cost);
        nbDigitalVideoDiscs++;
        this.setId(nbDigitalVideoDiscs);
    }

    public DigitalVideoDisc(String title, String category, String director, int length, float cost) {
        super(title, category, director, length, cost);
        nbDigitalVideoDiscs++;
        this.setId(nbDigitalVideoDiscs);
    }
    
    public String toString() {
        return "DVD - [" + getTitle() + "] - [" + getCategory() + "] - [" + getDirector() + 
               "] - [" + getLength() + " mins]: " + getCost() + " $";
    }

    public boolean isMatch(String title) {
        if (this.getTitle() == null || title == null) return false;
        return this.getTitle().equalsIgnoreCase(title);
    }

    @Override
    public void play() {
        System.out.println("Choi DVD: " + this.getTitle());
        System.out.println("DVD length: " + this.getLength());
    }
}
