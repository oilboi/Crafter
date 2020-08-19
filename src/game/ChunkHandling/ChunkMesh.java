package game.ChunkHandling;

import engine.graph.Mesh;
import engine.graph.Texture;

public class ChunkMesh {
    private Mesh mesh;
    private Texture texture;
    public ChunkMesh() throws Exception {
        // Create the Mesh
        float[] positions = new float[] {
                //front
                0.5f,  0.5f, 0.5f,
                -0.5f,  0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                //back
                -0.5f,  0.5f, -0.5f,
                0.5f,  0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                //right
                0.5f,  0.5f, -0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f, -0.5f,  0.5f,
                0.5f, -0.5f, -0.5f,
                //left
                -0.5f,  0.5f,  0.5f,
                -0.5f,  0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f,  0.5f,
                //top
                -0.5f,  0.5f, -0.5f,
                -0.5f,  0.5f,  0.5f,
                0.5f,  0.5f,  0.5f,
                0.5f,  0.5f, -0.5f,
                //bottom
                -0.5f, -0.5f,  0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f,  0.5f,
        };
        float frontLight  = 0.6f;
        float backLight   = 1.0f;
        float rightLight  = 0.3f;
        float leftLight   = 1.0f;
        float topLight    = 0.1f;
        float bottomLight = 1.0f;
        float[] colors = new float[]{
                //front
                frontLight,frontLight,frontLight,
                frontLight,frontLight,frontLight,
                frontLight,frontLight,frontLight,
                frontLight,frontLight,frontLight,

                //back
                backLight,backLight, backLight,
                backLight,backLight, backLight,
                backLight,backLight, backLight,
                backLight,backLight, backLight,

                //right
                rightLight,rightLight,rightLight,
                rightLight,rightLight,rightLight,
                rightLight,rightLight,rightLight,
                rightLight,rightLight,rightLight,

                //left
                leftLight,leftLight,leftLight,
                leftLight,leftLight,leftLight,
                leftLight,leftLight,leftLight,
                leftLight,leftLight,leftLight,

                //top
                topLight,topLight,topLight,
                topLight,topLight,topLight,
                topLight,topLight,topLight,
                topLight,topLight,topLight,

                //bottom
                bottomLight,bottomLight,bottomLight,
                bottomLight,bottomLight,bottomLight,
                bottomLight,bottomLight,bottomLight,
                bottomLight,bottomLight,bottomLight,

        };
        float[] textCoords = new float[]{
                //front
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                //back
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                //right
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                //left
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                //top
                0.5f, 0.5f,
                0.0f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                //bottom
                1.0f, 0.0f,
                0.5f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[] {
                //Front face
                0, 1, 2, 0, 2, 3,
                //Back face
                0+4, 1+4, 2+4, 0+4, 2+4, 3+4,
                //Right face
                0+8, 1+8, 2+8, 0+8, 2+8, 3+8,
                //Left face
                0+12, 1+12, 2+12, 0+12, 2+12, 3+12,
                //Top Face
                0+16, 1+16, 2+16, 0+16, 2+16, 3+16,
                //Bottom Face
                0+20, 1+20, 2+20, 0+20, 2+20, 3+20,
        };

        texture = new Texture("textures/grassblock.png");
        mesh = new Mesh(positions, colors, indices, textCoords, texture);
    }

    public Mesh getMesh(){
        return mesh;
    }

}
