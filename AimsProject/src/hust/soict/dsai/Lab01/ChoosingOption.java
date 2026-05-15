package hust.soict.dsai.Lab01;
import javax.swing.JOptionPane;
public class ChoosingOption {
    public static void main(String[] args) {
        int option = JOptionPane.showConfirmDialog(null, "Do you want to change to the first class ticket?");
        JOptionPane.showMessageDialog(null, "You've chosen: "+(option==JOptionPane.YES_OPTION?"Yes":"No"));
        System.exit(0);

    }
}
//khi an cancel thi code khong handle duoc , ham showConfirmDialog() se tra ve JOptionPane.CANCEL_OPTION , nhung dang mac dinh la YES_OPTION nen no se hien thi la "No"
//cach de tao them 2 option I do va i dont la tao 1 string option1 va nhan vao option1 trong showOptionDialog() , sau do if else option voi 0 va 1 de nhan result
//1 