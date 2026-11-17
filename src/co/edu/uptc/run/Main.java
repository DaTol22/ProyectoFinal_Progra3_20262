package co.edu.uptc.run;

import co.edu.uptc.model.BookableResource;
import co.edu.uptc.persistence.FileManager;
import co.edu.uptc.structures.SimpleList;

public class Main {
    public static void main(String[] args) {
        SimpleList<BookableResource> resources = FileManager.load();
        System.out.println(resources);
    }
}
