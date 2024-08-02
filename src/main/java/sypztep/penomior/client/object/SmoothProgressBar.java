package sypztep.penomior.client.object;

import net.minecraft.client.gui.DrawContext;

public class SmoothProgressBar extends Animation {
    private final int barWidth;  // Width of the progress bar
    private final int barHeight; // Height of the progress bar
    private final int barColor;  // Color of the filled part of the bar
    private final int backgroundColor; // Background color of the bar
    private float currentProgress; // Current progress value
    private float targetProgress; // Target progress value

    // Constructor
    public SmoothProgressBar(float duration, boolean isLooping, int barWidth, int barHeight, int barColor, int backgroundColor) {
        super(duration, isLooping);
        this.barWidth = barWidth;
        this.barHeight = barHeight;
        this.barColor = barColor;
        this.backgroundColor = backgroundColor;
        this.currentProgress = 0.0f;
        this.targetProgress = 0.0f;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        // Calculate the progress ratio using elapsed time and duration
        float progressRatio = Math.min(1.0f, elapsedTime / duration);
        float easedRatio = easeOutQuint(progressRatio);

        // Interpolate the current progress towards the target progress
        currentProgress = currentProgress + (targetProgress - currentProgress) * easedRatio;

        // Optionally clamp the currentProgress to the targetProgress
        if (currentProgress < targetProgress) {
            currentProgress = Math.min(currentProgress, targetProgress);
        } else if (currentProgress > targetProgress) {
            currentProgress = Math.max(currentProgress, targetProgress);
        }
    }

    // Ease-out quint function
    private float easeOutQuint(float t) {
        return (float) (1 - Math.pow(1 - t, 5));
    }

    // Set the target progress
    public void setProgress(float progress) {
        this.targetProgress = Math.max(0, Math.min(1, progress));
        this.elapsedTime = 0.0f; // Reset elapsed time for smooth transition
        this.isCompleted = false; // Reset completion status
    }

    public float getCurrentProgress() {
        return currentProgress;
    }

    public void render(DrawContext context, int x, int y) {
        float progress = Math.max(0, Math.min(1, currentProgress));
        int filledWidth = (int) (progress * barWidth);
        context.fill(x, y, x + barWidth, y + barHeight, backgroundColor);
        context.fill(x, y, x + filledWidth, y + barHeight, barColor);
    }
}

