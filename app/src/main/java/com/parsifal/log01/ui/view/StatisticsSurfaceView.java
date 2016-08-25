package com.parsifal.log01.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.parsifal.log01.R;
import com.parsifal.log01.utils.MathUtil;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by YangMing on 2016/8/25 09:06.
 */
public class StatisticsSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {

    private SurfaceHolder mHolder;

    private boolean run = false;

    private Paint mPaint = null;

    private int mBackgroundColor = 0;

    private int mDrawCount = 0;

    private String[] mSamples = null;

    private int mWidth = 0;

    private int mHeight = 0;

    private static final int radius = 3;

    private float screenX = 0.0f;

    private float screenY = 0.0f;

    private SimpleDateFormat mDateFormat = null;

    private static final String PATTERN = "HH:mm";

    private NormalDistribution normalDistribution = null;

    private static final int RECT_HALF = 10;

    private static final int X_AXES_MARGIN = 100;

    private static final int Y_AXES_MARGIN = 25;

    private static final int TEXT_X_PROOF1 = 6;

    private static final int TEXT_Y_PROOF1 = 5;

    private static final int TEXT_X_PROOF2 = 16;

    private static final int TEXT_Y_PROOF2 = 20;

    private static final float LINE_WIDTH_HALF = 0.5f;

    private static final float LINE_WIDTH = LINE_WIDTH_HALF * 2;

    private int Vermilion = ContextCompat.getColor(getContext(), R.color.Vermilion);

    private int Ruby = ContextCompat.getColor(getContext(), R.color.Ruby);

    private int PrussianBlue = ContextCompat.getColor(getContext(), R.color.PrussianBlue);

    private int PeacockBlue = ContextCompat.getColor(getContext(), R.color.PeacockBlue);

    public StatisticsSurfaceView(Context context) {
        super(context);
        init();
    }

    public StatisticsSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        mDateFormat = new SimpleDateFormat(PATTERN, Locale.getDefault());
        normalDistribution = new NormalDistribution();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(24.0f);
        mPaint.setAntiAlias(true);
    }

    public void setDataSource(String[] src) {
        mSamples = src;
        draw();
    }

    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, final int width, final int height) {
        mWidth = width;
        mHeight = height;
        draw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        run = false;
    }

    private void draw() {
        if (null != mSamples & 0 != mWidth & 0 != mHeight) {
            run = true;
        }
        if (run) {
            final int samplesCount = mSamples.length;
            double[] times = new double[samplesCount];
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < samplesCount; i++) {
                try {
                    String dateString = mSamples[i];
                    Date date = mDateFormat.parse(dateString);
                    calendar.setTime(date);
                    times[i] = calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            final double ц = MathUtil.getAverage(times);
            System.out.println("ц - " + ц);
            final double σ = MathUtil.getStandardDeviation(times);
            System.out.println("σ - " + σ);

            final double min = MathUtil.getMin(times);
            final double max = MathUtil.getMax(times);

            final int nodeCount = Double.valueOf(max - min + 1).intValue();
            System.out.println("nodeCount - " + nodeCount);

            double[] elements = new double[nodeCount];
            for (int i = 0; i < nodeCount; i++) {
                elements[i] = min + i;
            }
            final int[] ys = MathUtil.occurrences(elements, times);

            final String[] timeInterval = new String[ys.length];
            for (int i = 0; i < ys.length; i++) {
                int element = Double.valueOf(elements[i]).intValue();
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, element / 60);
                c.set(Calendar.MINUTE, element % 60);
                timeInterval[i] = mDateFormat.format(c.getTime());
            }

            final float eW = (mWidth - X_AXES_MARGIN) / (nodeCount);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (run) {
                        float lastX = 0;
                        float lastY = 0;

                        try {
                            Canvas canvas = mHolder.lockCanvas();
                            if (0 != mBackgroundColor) {
                                canvas.drawColor(mBackgroundColor);
                            }

                            mPaint.setColor(Ruby);
                            mPaint.setTextSize(48);
                            Calendar calendar = Calendar.getInstance();
                            int ц_int = new BigDecimal(ц).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                            calendar.set(Calendar.HOUR_OF_DAY, ц_int / 60);
                            calendar.set(Calendar.MINUTE, ц_int % 60);
                            canvas.drawText("ц = " + mDateFormat.format(calendar.getTime()), 2 * X_AXES_MARGIN, 2 * X_AXES_MARGIN, mPaint);

                            int σ_int = new BigDecimal(σ).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                            if (0 < σ_int) {
                                canvas.drawText("σ = " + σ_int + " min", 2 * X_AXES_MARGIN, 3 * X_AXES_MARGIN, mPaint);
                            } else {
                                canvas.drawText("σ = " + new BigDecimal(σ * 60).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + " s", 2 * X_AXES_MARGIN, 3 * X_AXES_MARGIN, mPaint);
                            }

                            float top = mHeight - X_AXES_MARGIN;
                            for (int i = 0; i < nodeCount; i++) {
                                float x = eW * i + X_AXES_MARGIN;
                                float bottom = mHeight - (ys[i] / Integer.valueOf(samplesCount).floatValue()) * mHeight - X_AXES_MARGIN;

                                mPaint.setColor(Vermilion);
                                canvas.drawRect(x - RECT_HALF, top, x + RECT_HALF, bottom, mPaint);

                                mPaint.setColor(Color.BLACK);
                                canvas.drawRect(Y_AXES_MARGIN, top + LINE_WIDTH, mWidth, top, mPaint);

                                canvas.drawRect(Y_AXES_MARGIN - LINE_WIDTH, top + LINE_WIDTH, Y_AXES_MARGIN, 0, mPaint);

                                if (0 != ys[i]) {
                                    mPaint.setColor(Ruby);
                                    mPaint.setTextSize(24);
                                    canvas.drawText(String.valueOf(ys[i]), x - TEXT_X_PROOF1, bottom - TEXT_Y_PROOF1, mPaint);

                                    mPaint.setColor(PrussianBlue);
                                    mPaint.setTextSize(12);
                                    canvas.drawText(timeInterval[i], x - TEXT_X_PROOF2, top + TEXT_Y_PROOF2, mPaint);
                                }
                            }

                            double i = -X_AXES_MARGIN / eW;
                            double increment = (nodeCount + X_AXES_MARGIN / eW) / 32.0d;
                            double x = 0.0d;
                            while (x <= mWidth) {
                                x = eW * i + X_AXES_MARGIN;
                                double z = ((min + i) - ц) / σ;
                                double y = normalDistribution.density(z);

                                canvas.drawLine(lastX, lastY, (float) x, (float) (mHeight - y * mHeight - X_AXES_MARGIN), mPaint);
                                lastX = (float) x;
                                lastY = (float) (mHeight - y * mHeight - X_AXES_MARGIN);
                                mPaint.setColor(PeacockBlue);
                                canvas.drawRect((float) ((ц - min) * eW - LINE_WIDTH_HALF + X_AXES_MARGIN), mHeight, (float) ((ц - min) * eW + LINE_WIDTH_HALF + X_AXES_MARGIN), 0, mPaint);
                                canvas.drawRect((float) ((ц - σ - min) * eW - LINE_WIDTH_HALF + X_AXES_MARGIN), mHeight, (float) ((ц - σ - min) * eW + LINE_WIDTH_HALF + X_AXES_MARGIN), 0, mPaint);
                                canvas.drawRect((float) ((ц + σ - min) * eW - LINE_WIDTH_HALF + X_AXES_MARGIN), mHeight, (float) ((ц + σ - min) * eW + LINE_WIDTH_HALF + X_AXES_MARGIN), 0, mPaint);
                                i += increment;
                            }

                            mHolder.unlockCanvasAndPost(canvas);

                            if (2 == mDrawCount) {
                                run = false;
                                mDrawCount = 0;
                            }
                            mDrawCount++;
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}
