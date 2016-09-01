package com.parsifal.log01.ui.view;

/**
 * Created by yangming on 16-9-1.
 */
public class StatisticsData {

    public double ц;

    public double σ;

    public double min;

    public double max;

    public double[] times;

    public StatisticsData() {

    }

    public StatisticsData(double ц, double σ, double min, double max, double[] times) {
        this.ц = ц;
        this.σ = σ;
        this.min = min;
        this.max = max;
        this.times = times;
    }
}
