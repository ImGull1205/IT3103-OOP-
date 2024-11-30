package Media.models;

import java.util.List;
import java.util.ArrayList;

public class CompactDisc extends Disc {
    private String artist;
    private List<Track> tracks;

    public CompactDisc(String title, String category, float cost) {
        super(title, category, cost);
        this.tracks = new ArrayList<>();
    }

    public CompactDisc(String title, String category, float cost, String artist) {
        this(title, category, cost);
        this.artist = artist;
    }

    public CompactDisc(String title, String category, String director, float cost, String artist) {
        super(title, category, cost, 0, director);
        this.artist = artist;
        this.tracks = new ArrayList<>();
    }

    public String getArtist() {
        return artist;
    }

    public void addTrack(Track track) {
        if (track == null) {
            System.out.println("Track cannot be null!");
            return;
        }
        
        if (tracks.contains(track)) {
            System.out.println("Track \"" + track.getTitle() + "\" is already in the CD!");
        } else {
            tracks.add(track);
            System.out.println("Track \"" + track.getTitle() + "\" has been added successfully!");
        }
    }

    public void removeTrack(Track track) {
        if (track == null) {
            System.out.println("Track cannot be null!");
            return;
        }
        
        if (tracks.contains(track)) {
            tracks.remove(track);
            System.out.println("Track \"" + track.getTitle() + "\" has been removed successfully!");
        } else {
            System.out.println("Track \"" + track.getTitle() + "\" is not found in the CD!");
        }
    }

    public int getLength() {
        int totalLength = 0;
        for (Track track : tracks) {
            totalLength += track.getLength();
        }
        return totalLength;
    }
}