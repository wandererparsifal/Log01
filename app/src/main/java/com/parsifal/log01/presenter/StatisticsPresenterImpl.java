package com.parsifal.log01.presenter;

import com.google.gson.Gson;
import com.parsifal.log01.bean.TimeAndPlace;
import com.parsifal.log01.utils.FileUtil;
import com.parsifal.log01.view.StatisticsView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by YangMing on 2016/8/24 16:02.
 */
public class StatisticsPresenterImpl implements StatisticsPresenter {

    private static final String TAG = StatisticsPresenterImpl.class.getSimpleName();

    private StatisticsView mStatisticsView = null;

    private FileUtil mFileUtil = FileUtil.getInstance();

    private SimpleDateFormat mDateFormat = null;

    private static final String PATTERN = "HH:mm";

    public StatisticsPresenterImpl(StatisticsView statisticsView) {
        mStatisticsView = statisticsView;
        mDateFormat = new SimpleDateFormat(PATTERN, Locale.getDefault());
    }

    @Override
    public void loadFromFile() {
        String json = mFileUtil.load();
        TimeAndPlace[] timeAndPlaces = new Gson().fromJson(json, TimeAndPlace[].class);
        int length = timeAndPlaces.length;
        LinkedList<String> sl1 = new LinkedList<String>();
        LinkedList<String> sl2 = new LinkedList<String>();
        TimeAndPlace timeAndPlace = null;
        for (int i = 0; i < length; i++) {
            timeAndPlace = timeAndPlaces[i];
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(timeAndPlace.date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (12 > hour) {
                sl1.add(mDateFormat.format(calendar.getTime()));
            } else {
                sl2.add(mDateFormat.format(calendar.getTime()));
            }
        }
        String[] samples1 = null;
        String[] samples2 = null;
        if (0 != sl1.size()) {
            samples1 = new String[sl1.size()];
            samples1 = sl1.toArray(samples1);
        }
        if (0 != sl2.size()) {
            samples2 = new String[sl2.size()];
            samples2 = sl2.toArray(samples2);
        }
        mStatisticsView.drawGraphics(samples1, samples2);
    }
}
