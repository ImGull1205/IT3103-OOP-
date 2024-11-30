package Media.models;

public class Disc extends Media {
    private int length;
    private String director;

    // Constructor cơ bản
    public Disc(String title, String category, float cost) {
        super(title, category, cost);
    }

    // Constructor đầy đủ
    public Disc(String title, String category, float cost, int length, String director) {
        super(title, category, cost);
        this.length = length;
        this.director = director;
    }

    // Getters
    public int getLength() {
        return length;
    }

    public String getDirector() {
        return director;
    }
}