package synth;

import javax.sound.sampled.LineUnavailableException;

public abstract class SoundSynth {

    public abstract void makeSound(int frequency, int durationMs) throws LineUnavailableException;
}
