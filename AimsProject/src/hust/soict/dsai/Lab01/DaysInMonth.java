package hust.soict.dsai.Lab01;
import java.util.Scanner;

public class DaysInMonth {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Nhap thang:");
        String strMonth = keyboard.nextLine().trim().toLowerCase();

        int month = -1;
        if (strMonth.matches("\\d+")) {
            month = Integer.parseInt(strMonth);
        } else {
            switch (strMonth) {
                case "january":
                case "jan.":
                case "jan":
                    month = 1;
                    break;
                case "february":
                case "feb.":
                case "feb":
                    month = 2;
                    break;
                case "march":
                case "mar.":
                case "mar":
                    month = 3;
                    break;
                case "april":
                case "apr.":
                case "apr":
                    month = 4;
                    break;
                case "may":
                    month = 5;
                    break;
                case "june":
                case "jun":
                    month = 6;
                    break;
                case "july":
                case "jul":
                    month = 7;
                    break;
                case "august":
                case "aug.":
                case "aug":
                    month = 8;
                    break;
                case "september":
                case "sept.":
                case "sept":
                case "sep":
                    month = 9;
                    break;
                case "october":
                case "oct.":
                case "oct":
                    month = 10;
                    break;
                case "november":
                case "nov.":
                case "nov":
                    month = 11;
                    break;
                case "december":
                case "dec.":
                case "dec":
                    month = 12;
                    break;
            }
        }

        System.out.println("Nhap nam:");
        int year = keyboard.nextInt();

        int days = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
        }

        System.out.println("Thang " + month + " nam " + year + " co " + days + " ngay.");
        keyboard.close();
    }
}
//1