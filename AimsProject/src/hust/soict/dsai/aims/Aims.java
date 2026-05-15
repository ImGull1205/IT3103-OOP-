package hust.soict.dsai.aims;

import hust.soict.dsai.aims.cart.Cart;
import hust.soict.dsai.aims.media.DigitalVideoDisc;
import hust.soict.dsai.aims.media.Media;
import hust.soict.dsai.aims.media.Playable;
import hust.soict.dsai.aims.store.Store;

import java.util.Collections;
import java.util.Scanner;

public class Aims {
    private static Store store = new Store();
    private static Cart cart = new Cart();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initData();

        int choice = -1;
        while (choice != 0) {
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    viewStore();
                    break;
                case 2:
                    updateStore();
                    break;
                case 3:
                    seeCurrentCart();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }
    }

    private static void initData() {
        DigitalVideoDisc dvd1 = new DigitalVideoDisc("The Lion King", "Animation", "Roger Allers", 87, 19.95f);
        DigitalVideoDisc dvd2 = new DigitalVideoDisc("Star Wars", "Science Fiction", "George Lucas", 87, 24.95f);
        DigitalVideoDisc dvd3 = new DigitalVideoDisc("Aladdin", "Animation", 18.99f);
        store.addMedia(dvd1);
        store.addMedia(dvd2);
        store.addMedia(dvd3);
    }

    public static void showMenu() {
        System.out.println("AIMS: ");
        System.out.println("--------------------------------");
        System.out.println("1. View store");
        System.out.println("2. Update store");
        System.out.println("3. See current cart");
        System.out.println("0. Exit");
        System.out.println("--------------------------------");
        System.out.println("Please choose a number: 0-1-2-3");
    }

    public static void storeMenu() {
        System.out.println("Options: ");
        System.out.println("--------------------------------");
        System.out.println("1. See a media's details");
        System.out.println("2. Add a media to cart");
        System.out.println("3. Play a media");
        System.out.println("4. See current cart");
        System.out.println("0. Back");
        System.out.println("--------------------------------");
        System.out.println("Please choose a number: 0-1-2-3-4");
    }

    private static void viewStore() {
        System.out.println("Store inventory:");
        for (Media m : store.getItemsInStore()) {
            System.out.println(m.toString());
        }

        int choice = -1;
        while (choice != 0) {
            storeMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    seeMediaDetails();
                    break;
                case 2:
                    addMediaToCart();
                    break;
                case 3:
                    playMedia();
                    break;
                case 4:
                    seeCurrentCart();
                    break;
                case 0:
                    System.out.println("Back to main menu.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void seeMediaDetails() {
        System.out.print("Enter the title of the media: ");
        String title = scanner.nextLine();
        Media media = store.fetchMedia(title);
        if (media != null) {
            System.out.println(media.toString());
            mediaDetailsMenu(media);
        } else {
            System.out.println("Media not found in store.");
        }
    }

    public static void mediaDetailsMenu(Media media) {
        int choice = -1;
        while (choice != 0) {
            System.out.println("Options: ");
            System.out.println("--------------------------------");
            System.out.println("1. Add to cart");
            System.out.println("2. Play");
            System.out.println("0. Back");
            System.out.println("--------------------------------");
            System.out.println("Please choose a number: 0-1-2");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    cart.addMedia(media);
                    break;
                case 2:
                    if (media instanceof Playable) {
                        ((Playable) media).play();
                    } else {
                        System.out.println("This media is not playable.");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addMediaToCart() {
        System.out.print("Enter the title of the media to add to cart: ");
        String title = scanner.nextLine();
        Media media = store.fetchMedia(title);
        if (media != null) {
            cart.addMedia(media);
            System.out.println("Current number of items in cart: " + cart.getItemsOrdered().size());
        } else {
            System.out.println("Media not found in store.");
        }
    }

    private static void playMedia() {
        System.out.print("Enter the title of the media to play: ");
        String title = scanner.nextLine();
        Media media = store.fetchMedia(title);
        if (media != null) {
            if (media instanceof Playable) {
                ((Playable) media).play();
            } else {
                System.out.println("This media is not playable.");
            }
        } else {
            System.out.println("Media not found in store.");
        }
    }

    private static void updateStore() {
        System.out.println("Options: ");
        System.out.println("--------------------------------");
        System.out.println("1. Add a media to store");
        System.out.println("2. Remove a media from store");
        System.out.println("0. Back");
        System.out.println("--------------------------------");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                System.out.print("Enter title: ");
                String title = scanner.nextLine();
                System.out.print("Enter category: ");
                String category = scanner.nextLine();
                System.out.print("Enter cost: ");
                float cost = scanner.nextFloat();
                scanner.nextLine();
                DigitalVideoDisc dvd = new DigitalVideoDisc(title, category, cost);
                store.addMedia(dvd);
                break;
            case 2:
                System.out.print("Enter the title of the media to remove: ");
                String titleToRemove = scanner.nextLine();
                Media m = store.fetchMedia(titleToRemove);
                if (m != null) {
                    store.removeMedia(m);
                } else {
                    System.out.println("Media not found in store.");
                }
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public static void cartMenu() {
        System.out.println("Options: ");
        System.out.println("--------------------------------");
        System.out.println("1. Filter medias in cart");
        System.out.println("2. Sort medias in cart");
        System.out.println("3. Remove media from cart");
        System.out.println("4. Play a media");
        System.out.println("5. Place order");
        System.out.println("0. Back");
        System.out.println("--------------------------------");
        System.out.println("Please choose a number: 0-1-2-3-4-5");
    }

    private static void seeCurrentCart() {
        cart.print();
        int choice = -1;
        while (choice != 0) {
            cartMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    filterMediasInCart();
                    break;
                case 2:
                    sortMediasInCart();
                    break;
                case 3:
                    System.out.print("Enter the title of the media to remove: ");
                    String title = scanner.nextLine();
                    Media media = cart.fetchMedia(title);
                    if (media != null) {
                        cart.removeMedia(media);
                    } else {
                        System.out.println("Media not found in cart.");
                    }
                    break;
                case 4:
                    playMediaInCart();
                    break;
                case 5:
                    System.out.println("An order is created.");
                    cart.empty();
                    break;
                case 0:
                    System.out.println("Back to previous menu.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void filterMediasInCart() {
        System.out.println("1. Filter by id");
        System.out.println("2. Filter by title");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            System.out.print("Enter id: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            cart.searchById(id);
        } else if (choice == 2) {
            System.out.print("Enter title: ");
            String title = scanner.nextLine();
            cart.searchByTitle(title);
        }
    }

    private static void sortMediasInCart() {
        System.out.println("1. Sort by title then cost");
        System.out.println("2. Sort by cost then title");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            Collections.sort(cart.getItemsOrdered(), Media.COMPARE_BY_TITLE_COST);
            cart.print();
        } else if (choice == 2) {
            Collections.sort(cart.getItemsOrdered(), Media.COMPARE_BY_COST_TITLE);
            cart.print();
        }
    }

    private static void playMediaInCart() {
        System.out.print("Enter the title of the media to play: ");
        String title = scanner.nextLine();
        Media media = cart.fetchMedia(title);
        if (media != null) {
            if (media instanceof Playable) {
                ((Playable) media).play();
            } else {
                System.out.println("This media is not playable.");
            }
        } else {
            System.out.println("Media not found in cart.");
        }
    }
}