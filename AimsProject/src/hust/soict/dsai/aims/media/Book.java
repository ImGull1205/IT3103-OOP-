package hust.soict.dsai.aims.media;

import java.util.ArrayList;
import java.util.List;

public class Book extends Media {
    private List<String> authors = new ArrayList<String>();

    public Book() {
        super();
    }

    public Book(String title) {
        super(title);
    }

    public Book(String title, String category, float cost) {
        super(title, category, cost);
    }

    public void addAuthor(String authorName) {
        if (!authors.contains(authorName)) {
            authors.add(authorName);
        } else {
            System.out.println("Tác giả đã tồn tại trong danh sách.");
        }
    }

    public void removeAuthor(String authorName) {
        if (authors.contains(authorName)) {
            authors.remove(authorName);
        } else {
            System.out.println("Không tìm thấy tác giả trong danh sách.");
        }
    }

    @Override
    public String toString() {
        return "Book - [" + getTitle() + "] - [" + getCategory() + "] - [" + String.join(", ", authors) + "] : " + getCost() + " $";
    }
}
