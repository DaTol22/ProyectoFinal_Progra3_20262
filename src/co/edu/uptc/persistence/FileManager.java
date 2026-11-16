package co.edu.uptc.persistence;

import co.edu.uptc.model.BookableResource;
import co.edu.uptc.structures.SimpleList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    public static SimpleList<BookableResource> load(){
        SimpleList<BookableResource> out = new SimpleList<>();
        String line;
        try(BufferedReader reader = Files.newBufferedReader(Path.of("src/co/edu/uptc/data/daticos.csv"))){
            while((line = reader.readLine()) != null){
                out.add();
            }
        }
        catch (IOException e){
            //TODO System.err.println(e.getMessage());
        }
    }

    private BookableResource elPutisimoFormat(String line){
        String[] lineArray = line.replace("\"", "").split(",");
        return new BookableResource(lineArray[0], lineArray[1],);
        //TODO FALTA ESTA MIERDA, A MIMIR
    }
}
