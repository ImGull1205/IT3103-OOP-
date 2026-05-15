package hust.soict.dsai.test.cart;

import hust.soict.dsai.aims.cart.Cart;
import hust.soict.dsai.aims.media.DigitalVideoDisc;

public class CartTest {
    public static void main(String[] args) {
        Cart cart = new Cart();
        DigitalVideoDisc dvd1 = new DigitalVideoDisc("The Lion King", 
                "Animation", "Roger Allers", 87, 19.95f);
        
        DigitalVideoDisc dvd2 = new DigitalVideoDisc("Star Wars", 
                "Science Fiction", "George Lucas", 87, 24.95f);
        
        DigitalVideoDisc dvd3 = new DigitalVideoDisc("Aladin", 
                "Animation", 18.99f);

        cart.addMedia(dvd1);
        cart.addMedia(dvd2);
        cart.addMedia(dvd3);
        cart.print();
        System.out.print("[Test 1] Search 'Star Wars': ");
        cart.searchByTitle("Star Wars");
        System.out.print("[Test 2] Search 'the lion king': ");
        cart.searchByTitle("the lion king");
        System.out.print("[Test 3] Search 'Jujutsu Kaisen': ");
        cart.searchByTitle("Jujutsu Kaisen");
    }
}