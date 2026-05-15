package hust.soict.dsai.aims.media;

public class Track implements Playable {
    private String title;
    private int length;

    public Track() {
    }

    public Track(String title, int length) {
        this.title = title;
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Track)) {
            return false;
        }
        Track track = (Track) obj;
        if (title == null) {
            return track.title == null && length == track.length;
        }
        return title.equals(track.title) && length == track.length;
    }

    @Override
    public void play() {
        System.out.println("Choi track: " + this.getTitle());
        System.out.println("Track length: " + this.getLength());
    }
}
