package hust.soict.dsai.test.polymorphism;

import java.util.ArrayList;
import java.util.List;

import hust.soict.dsai.aims.media.Book;
import hust.soict.dsai.aims.media.CompactDisc;
import hust.soict.dsai.aims.media.DigitalVideoDisc;
import hust.soict.dsai.aims.media.Media;

public class TestPolymorphism {
    public static void main(String[] args) {
        List<Media> mediae = new ArrayList<Media>();

        CompactDisc cd = new CompactDisc("CD Title", "Music", "Director", 60, 15.99f, "Artist");
        DigitalVideoDisc dvd = new DigitalVideoDisc("DVD Title", "Movie", "Director", 120, 20.99f);
        Book book = new Book("Book Title", "Education", 10.50f);

        mediae.add(cd);
        mediae.add(dvd);
        mediae.add(book);

        for (Media m : mediae) {
            System.out.println(m.toString());
        }
    }
}
