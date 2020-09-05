package engine.sound;

import game.Crafter;
import org.joml.Vector3f;

import static game.Crafter.getSoundManager;

public class SoundAPI {
    private static SoundManager soundManager = getSoundManager();

    public static void playSound(String name, Vector3f pos) throws Exception {
        SoundBuffer soundBuffer = new SoundBuffer("sounds/" + name + ".ogg");

        SoundSource thisSource = new SoundSource(false, false);

        thisSource.setBuffer(soundBuffer.getBufferId());

        thisSource.setPosition(pos);

        soundManager.playSoundSource(soundBuffer, thisSource);
    }
}
