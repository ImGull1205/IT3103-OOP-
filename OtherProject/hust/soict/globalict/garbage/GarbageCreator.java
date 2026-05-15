package hust.soict.globalict.garbage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class GarbageCreator {
    public static void main(String[] args) {
        String filename = "C:/Users/Khang Tran/Downloads/de-thi-thu-tot-nghiep-thpt-nam-2026-mon-toan-so-gddt-quang-ninh.pdf";
        byte[] inputBytes = { 0 };
        long start, end;

        try {
            inputBytes = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            System.err.println("Could not read file. Check filename!");
            return;
        }

        start = System.currentTimeMillis();
        String outputString = "";
        for (byte b : inputBytes) {
            outputString += (char) b;
        }
        end = System.currentTimeMillis();
        System.out.println("Processing time with +: " + (end - start) + "ms");
    }
}