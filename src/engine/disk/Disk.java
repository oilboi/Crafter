package engine.disk;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.graph.Mesh;
import game.chunk.ChunkObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Disk {



    public static void initializeWorldHandling(){
        createWorldsDir();
        createAlphaWorldFolder();
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
            objectMapper.writeValue(new File("Worlds/world1/" + thisChunk.ID + ".json"), thisChunk);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ChunkObject loadChunkFromDisk(int x, int z){
        ObjectMapper objectMapper = new ObjectMapper();

        String key = x + " " + z;

        ChunkObject thisChunk = null;

        File test = new File("Worlds/world1/" + key + ".json");

        if (!test.canRead()){
            return null;
        }

        try {
            thisChunk = objectMapper.readValue(test, ChunkObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        thisChunk.blockBoxMesh = new Mesh[8];
        thisChunk.liquidMesh = new Mesh[8];
        thisChunk.mesh = new Mesh[8];

        return thisChunk;
    }
}
