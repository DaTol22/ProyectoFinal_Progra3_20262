package co.edu.uptc.persistence;

import co.edu.uptc.model.*;
import co.edu.uptc.structures.SimpleList;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalTime;

public class FileManager {

    public static SimpleList<BookableResource> load(){
        SimpleList<BookableResource> out = new SimpleList<>();
        String line;
        try(BufferedReader reader = Files.newBufferedReader(Path.of("src/co/edu/uptc/data/resources.csv"))){
            reader.readLine();
            while((line = reader.readLine()) != null){
                out.add(format(line));
            }
        }
        catch (IOException e){
            //TODO System.err.println(e.getMessage());
        }
        return out;
    }

    private static BookableResource format(String line){
        String[] lineArray = line.replace("\"", "").split(",");
        String name = lineArray[0];
        String id = lineArray[1];
        LocalTime opens = LocalTime.parse(lineArray[2]);
        LocalTime closes = LocalTime.parse(lineArray[3]);
        if(lineArray[4].isEmpty() && lineArray[5].isEmpty()){
            return new SportsCourt(name, id, new OperatingHours(opens, closes));
        }else if(lineArray[5].isEmpty()){
            return new ParkingSpot(name, id, new OperatingHours(opens, closes), Integer.parseInt(lineArray[4]));
        }else{
            return new StudyRoom(name, id, new OperatingHours(opens, closes), Duration.ofMinutes(Long.parseLong(lineArray[5])));
        }
    }

    public static void save(){

    }
}
