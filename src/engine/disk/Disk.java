package engine.disk;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.Vector3Nullified;
import engine.graph.Mesh;
import game.chunk.ChunkObject;
import org.joml.Vector3f;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Disk {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
        try {
            objectMapper.writeValue(new File("Worlds/world1/" + thisChunk.ID + ".chunk"), thisChunk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChunkObject loadChunkFromDisk(int x, int z){
        String key = x + " " + z;

        ChunkObject thisChunk = null;

        File test = new File("Worlds/world1/" + key + ".chunk");

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
        thisChunk.modified = false;

        return thisChunk;
    }

    public static void savePlayerPos(Vector3f pos){
        Vector3Nullified tempPos = new Vector3Nullified();
        tempPos.x = pos.x;
        tempPos.y = pos.y;
        tempPos.z = pos.z;
        try {
            objectMapper.writeValue(new File("Worlds/world1/playerPos.data"), tempPos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Vector3f loadPlayerPos(){
        File test = new File("Worlds/world1/playerPos.data");

        Vector3f thisPos = new Vector3f(0,55,0);

        if (!test.canRead()){
            return thisPos;
        }

        Vector3Nullified tempPos = null;

        try {
            tempPos = objectMapper.readValue(test, Vector3Nullified.class);
        } catch (IOException e) {
            e.printStackTrace();

        }

        if (tempPos != null){
            thisPos.x = tempPos.x;
            thisPos.y = tempPos.y;
            thisPos.z = tempPos.z;
        }

        return thisPos;
    }
}
