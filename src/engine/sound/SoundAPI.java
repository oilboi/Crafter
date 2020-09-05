package engine.sound;

import game.Crafter;
import org.joml.Vector3f;

import static game.Crafter.getSoundManager;

public class SoundAPI {
    private static SoundManager soundManager = getSoundManager();

    public static void playSound(String name, Vector3f pos) throws Exception {
        SoundBuffer buffBack = new SoundBuffer("sounds/" + name + ".ogg");
        soundManager.addSoundBuffer(buffBack);
        SoundSource thisSource = new SoundSource(false, false);
        thisSource.setBuffer(buffBack.getBufferId());
        thisSource.setPosition(pos);
        soundManager.addSoundSource(name,thisSource);
        soundManager.playSoundSource(name);
        soundManager.removeSoundSource(name);
    }
}
