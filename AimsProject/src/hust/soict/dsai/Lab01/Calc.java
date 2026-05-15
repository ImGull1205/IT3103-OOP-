package hust.soict.dsai.Lab01;
import java.util.Scanner;

public class Calc {
    private double num1;
    private double num2;

    public Calc(double num1, double num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public double sum() {
        return num1 + num2;
    }

    public double difference() {
        return num1 - num2;
    }

    public double product() {
        return num1 * num2;
    }

    public String quotient() {
        if (num2 == 0.0) {
            return "Khong chia duoc cho 0";
        }
        return String.valueOf(num1 / num2);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("num 1: ");
        String strNum1 = scanner.nextLine();

        System.out.print("num 2: ");
        String strNum2 = scanner.nextLine();

        double num1;
        double num2;

        try {
            num1 = Double.parseDouble(strNum1);
            num2 = Double.parseDouble(strNum2);
        } catch (NumberFormatException e) {
            System.out.println("not double");
            scanner.close();
            return;
        }

        Calc calc = new Calc(num1, num2);

        System.out.println("tong: " + calc.sum());
        System.out.println("hieu: " + calc.difference());
        System.out.println("tich: " + calc.product());
        System.out.println("thuong: " + calc.quotient());

        scanner.close();
    }
}
//1
