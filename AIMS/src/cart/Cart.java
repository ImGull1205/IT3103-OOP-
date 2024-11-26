package cart;
import disc.DigitalVideoDisc;

public class Cart {
	public static final int MAX_NUMBERS_ORDERED = 20;

	private DigitalVideoDisc itemsOrdered[] = new DigitalVideoDisc[MAX_NUMBERS_ORDERED];
	private int qtyOrdered;

	public int addDigitalVideoDisc(DigitalVideoDisc disc) {
		if (qtyOrdered == MAX_NUMBERS_ORDERED) {
			System.out.println("Cart is full, cannot add!");
			return 0;
		} else {
			itemsOrdered[qtyOrdered] = disc;
			qtyOrdered++;
			System.out.println("The DVD \"" + disc.getTitle() + "\" has been added!");
			return 1;
		}
	}

	public int addDigitalVideoDisc(DigitalVideoDisc[] dvdList){
		int addCount = 0;
		for( DigitalVideoDisc disc : dvdList){
			if(qtyOrdered < MAX_NUMBERS_ORDERED){
				itemsOrdered[qtyOrdered] = disc;
				qtyOrdered++;
				System.out.println("The disc \"" + disc.getTitle() + "\" has been added!");
				addCount++;
			}
			else{
				System.out.println("The cart is full. Cannot add more items!");
				break;
			}
		}
		return addCount;
	}

	public int addDigitalVideoDisc(DigitalVideoDisc dvd1, DigitalVideoDisc dvd2) {
        int count = 0;
        if (qtyOrdered < MAX_NUMBERS_ORDERED) {
            itemsOrdered[qtyOrdered++] = dvd1;
            System.out.println("DVD \"" + dvd1.getTitle() + "\" added.");
            count++;
        } else {
            System.out.println("Cart is full!");
        }
        if (qtyOrdered < MAX_NUMBERS_ORDERED) {
            itemsOrdered[qtyOrdered++] = dvd2;
            System.out.println("DVD \"" + dvd2.getTitle() + "\" added.");
            count++;
        } else {
            System.out.println("Cart is full!");
        }
        return count;
    }


	public int removeDigitalVideoDisc(DigitalVideoDisc disc) {
		if (itemsOrdered[0] == null) {
			System.out.println("Your cart is empty!");
			return 0;
		}
		for (int i = 0; i < qtyOrdered; i++) {
			if (itemsOrdered[i].equals(disc)) {
				for (int j = i; i < qtyOrdered - 1; i++) {
					itemsOrdered[i] = itemsOrdered[i + 1];
				}
				itemsOrdered[qtyOrdered - 1] = null;
				qtyOrdered--;
				System.out.println("Remove DVD \"" + disc.getTitle() + "\" successfully!");
				return 1;
			}
		}
		System.out.println("No DVD match!");
		return 0;
	}

	public float totalCost() {
		float sum = 0.00f;
		for (int i = 0; i < qtyOrdered; i++) {
			sum += itemsOrdered[i].getCost();
		}
		return sum;
	}

	public void print() {
		StringBuilder output = new StringBuilder("CART: \n");
		for (int i = 0; i < qtyOrdered; i++) {
			output.append(i + 1 + ".[" + itemsOrdered[i].getTitle() + "] - [" + itemsOrdered[i].getCategory() + "] - ["
					+ itemsOrdered[i].getDirector() + "] - [" + itemsOrdered[i].getLength() + "]: "
					+ itemsOrdered[i].getCost() + " $\n");
		}
		output.append("total: ").append(totalCost()).append(" $\n");
		output.append("END CART!\n");
		System.out.println(output);
	}

	public void searchById(int i) {
		if (i > qtyOrdered) {
			System.out.println("No match found !");
		} else {
			System.out.println("Result: " + "[" + itemsOrdered[i - 1].getTitle() + "] - ["
					+ itemsOrdered[i - 1].getCategory() + "] - [" + itemsOrdered[i - 1].getDirector() + "] - ["
					+ itemsOrdered[i - 1].getLength() + "]: " + itemsOrdered[i - 1].getCost() + " $\n");
		}

	}

	public void searchByTitle(String title) {
		for (int i = 0; i < qtyOrdered; i++) {
			if (itemsOrdered[i].getTitle().equals(title)) {
				System.out.println("Result: " + "[" + itemsOrdered[i].getTitle() + "] - ["
						+ itemsOrdered[i].getCategory() + "] - [" + itemsOrdered[i].getDirector() + "] - ["
						+ itemsOrdered[i].getLength() + "]: " + itemsOrdered[i].getCost() + " $\n");
				return;
			}
		}
		System.out.println("No match found !");
	}
}
