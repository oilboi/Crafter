package engine.sound;

import org.joml.Vector3f;

import static engine.sound.SoundManager.playSoundSource;

public class SoundAPI {

    public static void playSound(String name, Vector3f pos) throws Exception {
        SoundBuffer soundBuffer = new SoundBuffer("sounds/" + name + ".ogg");

        SoundSource thisSource = new SoundSource(false, false);

        thisSource.setBuffer(soundBuffer.getBufferId());

        thisSource.setPosition(pos);

        playSoundSource(soundBuffer, thisSource);
    }
}
