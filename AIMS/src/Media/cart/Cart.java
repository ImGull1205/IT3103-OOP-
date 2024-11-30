package Media.cart;

import Media.models.Media;
import java.util.List;
import java.util.ArrayList;

public class Cart {
    public static final int MAX_NUMBERS_ORDERED = 20;
    private List<Media> itemsOrdered = new ArrayList<>();

    public int addMedia(Media media) {
        if (itemsOrdered.size() == MAX_NUMBERS_ORDERED) {
            System.out.println("Cart is full, cannot add!");
            return 0;
        } else {
            itemsOrdered.add(media);
            System.out.println("The media \"" + media.getTitle() + "\" has been added!");
            return 1;
        }
    }

    public int removeMedia(Media media) {
        if (itemsOrdered.isEmpty()) {
            System.out.println("Your cart is empty!");
            return 0;
        }
        if (itemsOrdered.remove(media)) {
            System.out.println("Remove media \"" + media.getTitle() + "\" successfully!");
            return 1;
        }
        System.out.println("No media match!");
        return 0;
    }

    public float totalCost() {
        float sum = 0.00f;
        for (Media item : itemsOrdered) {
            sum += item.getCost();
        }
        return sum;
    }

    public void print() {
        StringBuilder output = new StringBuilder("CART: \n");
        for (int i = 0; i < itemsOrdered.size(); i++) {
            Media item = itemsOrdered.get(i);
            output.append(i + 1).append(".[")
                  .append(item.getTitle()).append("] - [")
                  .append(item.getCategory()).append("]: ")
                  .append(item.getCost()).append(" $\n");
        }
        output.append("total: ").append(totalCost()).append(" $\n");
        output.append("END CART!\n");
        System.out.println(output);
    }

    public void searchById(int i) {
        if (i <= 0 || i > itemsOrdered.size()) {
            System.out.println("No match found!");
            return;
        }
        Media item = itemsOrdered.get(i - 1);
        System.out.println("Result: [" + item.getTitle() + "] - [" 
            + item.getCategory() + "]: " + item.getCost() + " $\n");
    }

    public void searchByTitle(String title) {
        for (Media item : itemsOrdered) {
            if (item.getTitle().equals(title)) {
                System.out.println("Result: [" + item.getTitle() + "] - ["
                    + item.getCategory() + "]: " + item.getCost() + " $\n");
                return;
            }
        }
        System.out.println("No match found!");
    }
}