package model;


public class Sound {
    private int frequency;
    private int durationMs;
    private String timbre;

    public Sound(int frequency, int durationMs, String timbre) {
        this.frequency = frequency;
        this.durationMs = durationMs;
        this.timbre = timbre;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public String getTimbre() {
        return timbre;
    }
}
