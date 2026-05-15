package hust.soict.dsai.Lab01;
import java.util.Scanner;

public class Matrices {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Dong:");
        int iRows = keyboard.nextInt();
        System.out.println("Cot:");
        int iCols = keyboard.nextInt();
        int[][] matrix1 = new int[iRows][iCols];
        int[][] matrix2 = new int[iRows][iCols];
        int[][] sumMatrix = new int[iRows][iCols];
        
        for (int i = 0; i < iRows; i++) {
            for (int j = 0; j < iCols; j++) {
                System.out.print("Matrix1[" + i + "][" + j + "]: ");
                matrix1[i][j] = keyboard.nextInt();
            }
        }
        for (int i = 0; i < iRows; i++) {
            for (int j = 0; j < iCols; j++) {
                System.out.print("Matrix2[" + i + "][" + j + "]: ");
                matrix2[i][j] = keyboard.nextInt();
            }
        }
        for (int i = 0; i < iRows; i++) {
            for (int j = 0; j < iCols; j++) {
                sumMatrix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        System.out.println("Tong :");
        for (int i = 0; i < iRows; i++) {
            for (int j = 0; j < iCols; j++) {
                System.out.print(sumMatrix[i][j]);
            }
            System.out.println(); 
        }
        keyboard.close();
    }
}