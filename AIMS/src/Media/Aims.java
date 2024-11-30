package Media;  

import java.util.ArrayList;
import java.util.List;

import Media.cart.Cart;
import Media.models.DigitalVideoDisc;
import Media.models.CompactDisc;
import Media.models.Book;  

public class Aims {
    public static void main(String[] args) {
        Cart anOrder = new Cart();
        
        DigitalVideoDisc[] dvdList = new DigitalVideoDisc[4];
        dvdList[0] = new DigitalVideoDisc("The Lion King", "Animation", "Roger Allers", 87, 19.95f);
        dvdList[1] = new DigitalVideoDisc("Star wars", "Science Fiction", "George Lucas", 87, 24.95f);
        dvdList[2] = new DigitalVideoDisc("Aladdin", "Animation", 18.99f);
        dvdList[3] = new DigitalVideoDisc("Persona 3: Winter of Rebirth", "Japanese Animation", "Tomohisa Taguchi", 87, 99.99f);

        CompactDisc[] cdList = new CompactDisc[2];
        cdList[0] = new CompactDisc("Nectar", "R&B record", 10.18f);  
        cdList[1] = new CompactDisc("Smithereen", "R&B/Pop", 11.98f, "88Rising");

        Book[] bookList = new Book[3];

        bookList[0] = new Book("Noruwei no mori", "Novel", 4.00f, 1);  
        bookList[0].addAuthor("Murakami Haruki");

        List<String> authors = new ArrayList<>();
        authors.add("Osamu Dazai");  
        bookList[1] = new Book("Ningen Shikkaku", "Novel", 9.60f, authors, 19);

        bookList[2] = new Book("The Glass Ocean", "Historical", 14.99f, 26);
        bookList[2].addAuthor("Beatriz Williams");  
        bookList[2].addAuthor("Lauren Willig"); 
        bookList[2].addAuthor("Karen White"); 
        bookList[2].addAuthor("Lmao.Co"); 

        for (DigitalVideoDisc dvd : dvdList) {
            anOrder.addMedia(dvd);
        }

        for (CompactDisc cd : cdList) {  
            anOrder.addMedia(cd);
        }

        for (Book book : bookList) {
            anOrder.addMedia(book);
        }
        anOrder.print();
        anOrder.removeMedia(dvdList[1]); 
        bookList[2].removeAuthor("Lmao.Co");
        anOrder.searchByTitle("Persona 3: Winter of Rebirth");
        
        System.out.printf("Total cost is: %.2f", anOrder.totalCost());
    }
}