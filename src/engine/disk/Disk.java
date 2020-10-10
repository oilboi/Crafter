package engine.disk;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.chunk.ChunkObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Disk {



    public static void initializeWorldHandling(){
        createWorldsDir();
//        createAlphaWorldFolder();
    }

    private static void createWorldsDir(){
        try {
            Files.createDirectories(Paths.get("Worlds"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAlphaWorldFolder(){
        try {
            Files.createDirectories(Paths.get("Worlds/world1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveChunk(ChunkObject thisChunk){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("Worlds/" + thisChunk.ID + ".json"), thisChunk);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ChunkObject example = null;
//
//        File test = new File("Worlds/test.json");
//
//        try {
//            example = objectMapper.readValue(test, ChunkObject.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        if (example != null){
//            System.out.println(example.heightMap[0][0]);
//        }
    }
}
