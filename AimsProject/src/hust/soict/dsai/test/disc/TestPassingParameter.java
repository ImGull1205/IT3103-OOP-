package hust.soict.dsai.test.disc;

import hust.soict.dsai.aims.media.DigitalVideoDisc;

public class TestPassingParameter {

    public static void main(String[] args) {
        DigitalVideoDisc jungleDVD = new DigitalVideoDisc("Jungle");
        DigitalVideoDisc cocoDVD = new DigitalVideoDisc("Coco");
        swap(jungleDVD, cocoDVD);
        System.out.println("jungle dvd title: " + jungleDVD.getTitle());
        System.out.println("coco dvd title: " + cocoDVD.getTitle());

    }
    public static void swap(Object o1, Object o2) {
        Object tmp = o1;
        o1 = o2;
        o2 = tmp;
    }

    
}