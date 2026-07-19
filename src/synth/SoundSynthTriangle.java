package synth;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundSynthTriangle extends SoundSynth {

    @Override
    public void makeSound(int frequency, int durationMs) throws LineUnavailableException {
        byte[] buf = new byte[2];
        int sampleRate = 44100;
        AudioFormat af = new AudioFormat((float) sampleRate, 16, 1, true, false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open();
        sdl.start();
        for (int i = 0; i < durationMs * (float) sampleRate / 1000; i++) {
            float samplesPerCycle = (float) sampleRate / frequency;
            double phase = (i % samplesPerCycle) / samplesPerCycle;

            double value = 4 * Math.abs(phase - 0.5) - 1;

            short a = (short) (value * 32767);
            buf[0] = (byte) (a & 0xFF);
            buf[1] = (byte) (a >> 8);
            sdl.write(buf, 0, 2);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
}
