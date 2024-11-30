package Media.models;

import java.util.List;
import java.util.ArrayList;

public class Book extends Media {
    private List<String> authors;

    public Book(String title, String category, float cost, int id) {
        super(title, category, cost);
        this.authors = new ArrayList<>();
        this.id = id;
    }

    public Book(String title, String category, float cost, List<String> authors, int id) {
        super(title, category, cost);
        if (authors == null) {
            this.authors = new ArrayList<>();
        } else {
            this.authors = new ArrayList<>(authors);
        }
        this.id = id;
    }

    public int addAuthor(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            System.out.println("Author name cannot be empty!");
            return 0;
        }

        for (String author : authors) {
            if (author.equalsIgnoreCase(authorName)) {
                System.out.println("Author \"" + authorName + "\" is already in the list!");
                return 0;
            }
        }

        authors.add(authorName);
        System.out.println("Author \"" + authorName + "\" has been added successfully!");
        return 1;
    }

    public int removeAuthor(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            System.out.println("Author name cannot be empty!");
            return 0;
        }

        if (authors.isEmpty()) {
            System.out.println("The authors list is empty!");
            return 0;
        }

        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).equalsIgnoreCase(authorName)) {
                authors.remove(i);
                System.out.println("Author \"" + authorName + "\" has been removed successfully!");
                return 1;
            }
        }

        System.out.println("Author \"" + authorName + "\" is not found!");
        return 0;
    }

    public List<String> getAuthors() {
        return new ArrayList<>(authors); 
    }
}