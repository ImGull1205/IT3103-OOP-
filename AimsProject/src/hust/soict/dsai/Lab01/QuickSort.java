package hust.soict.dsai.Lab01;
import java.util.Scanner;
import java.util.Arrays;

public class QuickSort { 
    public static void quickSort(double[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);  
            quickSort(arr, pi + 1, high); 
        }
    }

    public static int partition(double[] arr, int low, int high) {
        double pivot = arr[high]; 
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                double temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        double temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("n:");
        int n = keyboard.nextInt();
        double[] my_array1 = new double[n];

        System.out.println("Nhap mang ");
        for (int i = 0; i < n; i++) {
            my_array1[i] = keyboard.nextDouble();
        }
        System.out.println("\nOriginal array: " + Arrays.toString(my_array1));
        quickSort(my_array1, 0, n - 1);
        System.out.println("Sorted array: " + Arrays.toString(my_array1));
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += my_array1[i];
        }
        double average = (n > 0) ? (sum / n) : 0;
        System.out.println("Tong: " + sum);
        System.out.println("Trung binh: " + average);

        keyboard.close();
    }
} 
//1