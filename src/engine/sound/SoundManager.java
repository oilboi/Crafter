package engine.sound;

import engine.graph.Camera;
import engine.graph.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.alDistanceModel;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SoundManager {
    private long device;
    private long context;
    private SoundListener listener;

    private SoundBuffer[] soundBufferList;

    private SoundSource[] soundSourceArray;

    private final Matrix4f cameraMatrix;

    private int currentIndex = 0;

    public SoundManager(){
        soundBufferList = new SoundBuffer[32];
        soundSourceArray = new SoundSource[32];
        cameraMatrix = new Matrix4f();
    }

    public void init() throws Exception{
        this.device = alcOpenDevice((ByteBuffer) null);

        if(device == NULL){
            throw new IllegalStateException("Failed to open the default OpenAL device");
        }

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        this.context = alcCreateContext(device, (IntBuffer) null);

        if(context == NULL){
            throw new IllegalStateException("Failed to create OpenAL context");
        }

        alcMakeContextCurrent(context);

        AL.createCapabilities(deviceCaps);
    }

    public void playSoundSource(SoundBuffer soundBuffer, SoundSource soundSource) {

        if (soundBufferList[currentIndex] != null){
            soundBufferList[currentIndex].cleanUp();
        }
        soundBufferList[currentIndex] = soundBuffer;


        if (soundSourceArray[currentIndex] != null) {
            soundSourceArray[currentIndex].stop();
            soundSourceArray[currentIndex].cleanUp();
        }
        this.soundSourceArray[currentIndex] = soundSource;
        this.soundSourceArray[currentIndex].play();

        currentIndex++;
        if (currentIndex >= 32){
            currentIndex = 0;
        }
    }


    public SoundListener getListener() {
        return this.listener;
    }

    public void setListener(SoundListener listener) {
        this.listener = listener;
    }

    public void updateListenerPosition(Camera camera) {
        // Update camera matrix with camera data
        Transformation.updateGenericViewMatrix(camera.getPosition(), camera.getRotation(), cameraMatrix);
        listener.setPosition(camera.getPosition());
        Vector3f at = new Vector3f();
        cameraMatrix.positiveZ(at).negate();
        Vector3f up = new Vector3f();
        cameraMatrix.positiveY(up);
        listener.setOrientation(at, up);
    }

    public void setAttenuationModel(int model) {
        alDistanceModel(model);
    }

    public void cleanup() {
        for (SoundSource soundSource : soundSourceArray) {
            soundSource.cleanUp();
        }

        soundSourceArray = null;

        for (SoundBuffer soundBuffer : soundBufferList) {
            soundBuffer.cleanUp();
        }

        soundBufferList = null;

        if (context != NULL) {
            alcDestroyContext(context);
        }
        if (device != NULL) {
            alcCloseDevice(device);
        }
    }
}
