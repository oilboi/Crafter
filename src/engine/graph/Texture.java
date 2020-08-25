package engine.graph;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private final int id;

    public Texture(String fileName) throws Exception{
        this(loadTexture(fileName));
    }

    public Texture(int id){
        this.id = id;
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getId(){
        return id;
    }

    private static int loadTexture(String fileName) throws Exception{
        int width;
        int height;
        ByteBuffer buf;
        //Load texture file
        try (MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = stbi_load(fileName, w, h, channels, 4);

            if (buf == null){
                throw new Exception("Image file [" + fileName + "] not loaded: " + stbi_failure_reason());
            }

            //get width and height of image
            width = w.get();
            height = h.get();
        }

        //create a new openGL texture
        int textureId = glGenTextures();

        //bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);

        //tell openGL how to unpack the RGBA bytes. Each component is 1 byte in size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        //turn off texture filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        //generate mipmap
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(buf);

        return textureId;
    }

    public void cleanup(){
        glDeleteTextures(id);
    }
}
