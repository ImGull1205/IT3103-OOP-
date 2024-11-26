import cart.Cart;
import disc.DigitalVideoDisc;

public class Aims {
    public static void main(String[] args) {
        Cart anOrder = new Cart();

        // Tạo một mảng DigitalVideoDisc
        DigitalVideoDisc[] dvdList = new DigitalVideoDisc[4];

        dvdList[0] = new DigitalVideoDisc("The Lion King","Animation", "Roger Allers", 87, 19.95f);
        dvdList[1] = new DigitalVideoDisc("Star wars","Science Fiction", "Geogre Lucas", 87, 24.95f);
        dvdList[2] = new DigitalVideoDisc("Aladin","Animation", 18.99f);
        dvdList[3] = new DigitalVideoDisc("Persona 3 : Winter of Rebirth","Japanese Animation", "Tomohisa Taguchi", 87, 99.99f);

        // Sử dụng phương thức addDigitalVideoDisc(DigitalVideoDisc[] dvdList)
        anOrder.addDigitalVideoDisc(dvdList);

        anOrder.print();

        anOrder.removeDigitalVideoDisc(dvdList[1]); //remove dvd2
        System.out.printf("Total cost is: %.2f", anOrder.totalCost());
    }
}