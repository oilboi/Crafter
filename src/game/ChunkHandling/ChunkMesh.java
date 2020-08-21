package game.ChunkHandling;

import engine.graph.Mesh;
import engine.graph.Texture;

import java.util.ArrayList;

public class ChunkMesh {
    private Mesh mesh;
    private Texture texture;
    public ChunkMesh(Chunk chunk, int chunkX, int chunkZ) throws Exception {

        //why yes, this is a terrible way to do this
        //if you know a better way, feel free to make a pr

        int x = 0;
        int y = 0;
        int z = 0;

        int indicesCount = 0;

        ArrayList positions     = new ArrayList();
        ArrayList textureCoord  = new ArrayList();
        ArrayList indices       = new ArrayList();
        ArrayList light         = new ArrayList();


        int offsetX = chunkX * 16;
        int offsetZ = chunkZ * 16;

        short neighborBlock;

        //create the mesh
        for (int w = 0; w < (16 * 128 * 16); w++) {
            short blockID = chunk.getBlock(x,y,z, chunkX, chunkZ);

            if (blockID != 0) {
                //here for debug right now
                float frontLight  = (float)Math.random();//1.0f;
                float backLight   = (float)Math.random();//1.0f;
                float rightLight  = (float)Math.random();//1.0f;
                float leftLight   = (float)Math.random();//1.0f;
                float topLight    = (float)Math.random();//1.0f;
                float bottomLight = (float)Math.random();//1.0f;

                neighborBlock = chunk.getBlock(x, y, z+1, 0, 0);
                if (neighborBlock == 0) {
                    //front
                    positions.add(1f + x + offsetX); positions.add(1f + y); positions.add(1f + z + offsetZ);
                    positions.add(0f + x + offsetX); positions.add(1f + y); positions.add(1f + z + offsetZ);
                    positions.add(0f + x + offsetX); positions.add(0f + y); positions.add(1f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(0f + y); positions.add(1f + z + offsetZ);
                    //front
                    for (int i = 0; i < 12; i++){
                        light.add(frontLight);
                    }
                    //front
                    indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
                    indicesCount += 4;
                    //front
                    textureCoord.add(0.5f);textureCoord.add(0.0f);
                    textureCoord.add(0.0f);textureCoord.add(0.0f);
                    textureCoord.add(0.0f);textureCoord.add(0.5f);
                    textureCoord.add(0.5f);textureCoord.add(0.5f);
                }


                neighborBlock = chunk.getBlock(x, y, z-1, 0, 0);
                if (neighborBlock == 0) {
                    //back
                    positions.add(0f + x + offsetX); positions.add(1f + y); positions.add(0f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(1f + y); positions.add(0f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(0f + y); positions.add(0f + z + offsetZ);
                    positions.add(0f + x + offsetX); positions.add(0f + y); positions.add(0f + z + offsetZ);
                    //back
                    for (int i = 0; i < 12; i++){
                        light.add(backLight);
                    }
                    //back
                    indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
                    indicesCount += 4;
                    //back
                    textureCoord.add(0.5f);textureCoord.add(0.0f);
                    textureCoord.add(0.0f);textureCoord.add(0.0f);
                    textureCoord.add(0.0f);textureCoord.add(0.5f);
                    textureCoord.add(0.5f);textureCoord.add(0.5f);
                }

                neighborBlock = chunk.getBlock(x+1, y, z, 0, 0);
                if (neighborBlock == 0) {
                    //right
                    positions.add(1f + x + offsetX); positions.add(1f + y); positions.add(0f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(1f + y); positions.add(1f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(0f + y); positions.add(1f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(0f + y); positions.add(0f + z + offsetZ);
                    //right
                    for (int i = 0; i < 12; i++){
                        light.add(rightLight);
                    }
                    //right
                    indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
                    indicesCount += 4;
                    //right
                    textureCoord.add(0.5f);textureCoord.add(0.0f);
                    textureCoord.add(0.0f);textureCoord.add(0.0f);
                    textureCoord.add(0.0f);textureCoord.add(0.5f);
                    textureCoord.add(0.5f);textureCoord.add(0.5f);
                }

                neighborBlock = chunk.getBlock(x-1, y, z, 0, 0);
                if (neighborBlock == 0) {
                    //left
                    positions.add(0f + x + offsetX); positions.add(1f + y); positions.add(1f + z + offsetZ);
                    positions.add(0f + x + offsetX); positions.add(1f + y); positions.add(0f + z + offsetZ);
                    positions.add(0f + x + offsetX); positions.add(0f + y); positions.add(0f + z + offsetZ);
                    positions.add(0f + x + offsetX); positions.add(0f + y); positions.add(1f + z + offsetZ);
                    //left
                    for (int i = 0; i < 12; i++){
                        light.add(leftLight);
                    }
                    //left
                    indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
                    indicesCount += 4;
                    //left
                    textureCoord.add(0.5f);textureCoord.add(0.0f);
                    textureCoord.add(0.0f);textureCoord.add(0.0f);
                    textureCoord.add(0.0f);textureCoord.add(0.5f);
                    textureCoord.add(0.5f);textureCoord.add(0.5f);
                }

                neighborBlock = chunk.getBlock(x, y+1, z, 0, 0);
                if (neighborBlock == 0) {
                    //top
                    positions.add(0f + x + offsetX); positions.add(1f + y); positions.add(0f + z + offsetZ);
                    positions.add(0f + x + offsetX); positions.add(1f + y); positions.add(1f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(1f + y); positions.add(1f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(1f + y); positions.add(0f + z + offsetZ);
                    //top
                    for (int i = 0; i < 12; i++){
                        light.add(topLight);
                    }
                    //top
                    indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
                    indicesCount += 4;
                    //top
                    textureCoord.add(0.5f);textureCoord.add(0.5f);
                    textureCoord.add(0.0f);textureCoord.add(0.5f);
                    textureCoord.add(0.0f);textureCoord.add(1.0f);
                    textureCoord.add(0.5f);textureCoord.add(1.0f);
                }

                neighborBlock = chunk.getBlock(x, y-1, z, 0, 0);
                if (neighborBlock == 0) {
                    //bottom
                    positions.add(0f + x + offsetX); positions.add(0f + y);positions.add(1f + z + offsetZ);
                    positions.add(0f + x + offsetX); positions.add(0f + y);positions.add(0f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(0f + y);positions.add(0f + z + offsetZ);
                    positions.add(1f + x + offsetX); positions.add(0f + y);positions.add(1f + z + offsetZ);
                    //bottom
                    for (int i = 0; i < 12; i++){
                        light.add(bottomLight);
                    }
                    //bottom
                    indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
                    indicesCount += 4;
                    //bottom
                    textureCoord.add(1.0f);textureCoord.add(0.0f);
                    textureCoord.add(0.5f);textureCoord.add(0.0f);
                    textureCoord.add(0.5f);textureCoord.add(0.5f);
                    textureCoord.add(1.0f);textureCoord.add(0.5f);
                }
            }
            y++;
            if( y > 128 - 1 ){
                y = 0;
                x++;
                if( x > 16 - 1 ){
                    x = 0;
                    z++;
                }
            }
        }

        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float)positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float)light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int)indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float)textureCoord.get(i);
        }


        texture = new Texture("textures/grassblock.png");
        mesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, texture);
    }

    public Mesh getMesh(){
        return mesh;
    }

}
