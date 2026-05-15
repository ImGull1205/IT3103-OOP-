package hust.soict.dsai.Lab01;
import java.util.Scanner;

public class EquationSolver {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Menu");
        System.out.println("1.ax + b = 0");
        System.out.println("2.He phuong trinh 2 bien");
        System.out.println("3.ax^2 + bx + c = 0");
        System.out.print("Chon 1-3: ");
        int choice = keyboard.nextInt();

        switch (choice) {
            case 1:
                System.out.println("ax + b = 0");
                System.out.print("a: ");
                double a = keyboard.nextDouble();
                System.out.print("b: ");
                double b = keyboard.nextDouble();

                if (a == 0) {
                    if (b == 0) {
                        System.out.println("Vo so nghiem");
                    } else {
                        System.out.println("Vo nghiem");
                    }
                } else {
                    double x = -b / a;
                    System.out.println("Nghiem :x = " + x);
                }
                break;

            case 2:
                System.out.println("He phuong trinh 2 bien");
                System.out.print("a11: "); double a11 = keyboard.nextDouble();
                System.out.print("a12: "); double a12 = keyboard.nextDouble();
                System.out.print("b1: ");  double b1 = keyboard.nextDouble();
                System.out.print("a21: "); double a21 = keyboard.nextDouble();
                System.out.print("a22: "); double a22 = keyboard.nextDouble();
                System.out.print("b2: ");  double b2 = keyboard.nextDouble();

                double D = a11 * a22 - a21 * a12;
                double D1 = b1 * a22 - b2 * a12;
                double D2 = a11 * b2 - a21 * b1;

                if (D != 0) {
                    double x1 = D1 / D;
                    double x2 = D2 / D;
                    System.out.println("x1 = " + x1 + " and x2 = " + x2);
                } else {
                    if (D1 == 0 && D2 == 0) {
                        System.out.println("Vo so nghiem");
                    } else {
                        System.out.println("Vo nghiem");
                    }
                }
                break;

            case 3:
                System.out.println("ax^2 + bx + c = 0");
                System.out.print("a: "); double a2 = keyboard.nextDouble();
                System.out.print("b: "); double b2_quad = keyboard.nextDouble();
                System.out.print("c: "); double c2 = keyboard.nextDouble();

                if (a2 == 0) {
                    if (b2_quad == 0) {
                        if (c2 == 0) System.out.println("Vo so nghiem");
                        else System.out.println("Vo nghiem");
                    } else {
                        System.out.println("x = " + (-c2 / b2_quad));
                    }
                } else {
                    double delta = b2_quad * b2_quad - 4 * a2 * c2;
                    if (delta < 0) {
                        System.out.println("Khong co nghiem thuc");
                    } else if (delta == 0) {
                        double x = -b2_quad / (2 * a2);
                        System.out.println("Nghiem kep: x = " + x);
                    } else {
                        double x1 = (-b2_quad + Math.sqrt(delta)) / (2 * a2);
                        double x2 = (-b2_quad - Math.sqrt(delta)) / (2 * a2);
                        System.out.println("x1 = " + x1 + " and x2 = " + x2);
                    }
                }
                break;

            default:
                System.out.println("Chon sai!");
                break;
        }
        
        keyboard.close();
    }
}
//1