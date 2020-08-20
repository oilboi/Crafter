package game.ChunkHandling;

import engine.graph.Mesh;
import engine.graph.Texture;

import java.util.ArrayList;

public class ChunkMesh {
    private Mesh mesh;
    private Texture texture;
    public ChunkMesh() throws Exception {

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

        //here for debug right now
        float frontLight  = 1.0f;
        float backLight   = 1.0f;
        float rightLight  = 1.0f;
        float leftLight   = 1.0f;
        float topLight    = 1.0f;
        float bottomLight = 1.0f;

        //create the mesh

        //front
        positions.add(1f); positions.add(1f); positions.add(1f);
        positions.add(0f); positions.add(1f); positions.add(1f);
        positions.add(0f); positions.add(0f); positions.add(1f);
        positions.add(1f); positions.add(0f); positions.add( 1f);
        //back
        positions.add(0f); positions.add(1f); positions.add(0f);
        positions.add(1f); positions.add(1f); positions.add(0f);
        positions.add(1f); positions.add(0f); positions.add(0f);
        positions.add(0f); positions.add(0f); positions.add(0f);
        //right
        positions.add(1f); positions.add(1f); positions.add(0f);
        positions.add(1f); positions.add(1f); positions.add(1f);
        positions.add(1f); positions.add(0f); positions.add(1f);
        positions.add(1f); positions.add(0f); positions.add(0f);

        //left
        positions.add(0f); positions.add(1f); positions.add(1f);
        positions.add(0f); positions.add(1f); positions.add(0f);
        positions.add(0f); positions.add(0f); positions.add(0f);
        positions.add(0f); positions.add(0f); positions.add(1f);

        //top
        positions.add(0f); positions.add(1f); positions.add(0f);
        positions.add(0f); positions.add(1f); positions.add(1f);
        positions.add(1f); positions.add(1f); positions.add(1f);
        positions.add(1f); positions.add(1f); positions.add(0f);

        //bottom
        positions.add(0f);positions.add(0f);positions.add(1f);
        positions.add(0f);positions.add(0f);positions.add(0f);
        positions.add(1f);positions.add(0f);positions.add(0f);
        positions.add(1f);positions.add(0f);positions.add(1f);

        //front
        for (int i = 0; i < 12; i++){
            light.add(frontLight);
        }
        //back
        for (int i = 0; i < 12; i++){
            light.add(backLight);
        }
        //right
        for (int i = 0; i < 12; i++){
            light.add(rightLight);
        }
        //left
        for (int i = 0; i < 12; i++){
            light.add(leftLight);
        }
        //top
        for (int i = 0; i < 12; i++){
            light.add(topLight);
        }
        //bottom
        for (int i = 0; i < 12; i++){
            light.add(bottomLight);
        }

        //this is horrible

        //front
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;
        //back
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;
        //right
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;
        //left
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;
        //top
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;
        //bottom
        indices.add(0+indicesCount); indices.add(1+indicesCount); indices.add(2+indicesCount); indices.add(0+indicesCount); indices.add(2+indicesCount); indices.add(3+indicesCount);
        indicesCount += 4;


        //front
        textureCoord.add(0.5f);textureCoord.add(0.0f);
        textureCoord.add(0.0f);textureCoord.add(0.0f);
        textureCoord.add(0.0f);textureCoord.add(0.5f);
        textureCoord.add(0.5f);textureCoord.add(0.5f);
        //back
        textureCoord.add(0.5f);textureCoord.add(0.0f);
        textureCoord.add(0.0f);textureCoord.add(0.0f);
        textureCoord.add(0.0f);textureCoord.add(0.5f);
        textureCoord.add(0.5f);textureCoord.add(0.5f);
        //right
        textureCoord.add(0.5f);textureCoord.add(0.0f);
        textureCoord.add(0.0f);textureCoord.add(0.0f);
        textureCoord.add(0.0f);textureCoord.add(0.5f);
        textureCoord.add(0.5f);textureCoord.add(0.5f);
        //left
        textureCoord.add(0.5f);textureCoord.add(0.0f);
        textureCoord.add(0.0f);textureCoord.add(0.0f);
        textureCoord.add(0.0f);textureCoord.add(0.5f);
        textureCoord.add(0.5f);textureCoord.add(0.5f);
        //top
        textureCoord.add(0.5f);textureCoord.add(0.5f);
        textureCoord.add(0.0f);textureCoord.add(0.5f);
        textureCoord.add(0.0f);textureCoord.add(1.0f);
        textureCoord.add(0.5f);textureCoord.add(1.0f);
        //bottom
        textureCoord.add(1.0f);textureCoord.add(0.0f);
        textureCoord.add(0.5f);textureCoord.add(0.0f);
        textureCoord.add(0.5f);textureCoord.add(0.5f);
        textureCoord.add(1.0f);textureCoord.add(0.5f);


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
