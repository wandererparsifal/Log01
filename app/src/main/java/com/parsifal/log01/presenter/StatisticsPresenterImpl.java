package com.parsifal.log01.presenter;

import android.util.Log;

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
        Log.i(TAG, "loadFromFile json " + json);
        if (null != json) {
            TimeAndPlace[] timeAndPlaces = new Gson().fromJson(json, TimeAndPlace[].class);
            int length = timeAndPlaces.length;
            LinkedList<Calendar> calendars = new LinkedList<Calendar>();

            LinkedList<String> list_work = new LinkedList<String>();
            LinkedList<String> list_home = new LinkedList<String>();
            TimeAndPlace timeAndPlace = null;
            int day_before = 0;
            for (int i = 0; i < length; i++) {
                timeAndPlace = timeAndPlaces[i];
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timeAndPlace.date);
                day_before = calendar.get(Calendar.DAY_OF_MONTH);
                if (0 == calendars.size() || calendars.getLast().get(Calendar.DAY_OF_MONTH) == day_before) {
                    calendars.add(calendar);
                } else {
                    Calendar first = calendars.getFirst();
                    int hour_first = first.get(Calendar.HOUR_OF_DAY);
                    if (12 > hour_first) {
                        list_work.add(mDateFormat.format(first.getTime()));
                    }
                    Calendar last = calendars.getLast();
                    int hour_last = last.get(Calendar.HOUR_OF_DAY);
                    if (12 <= hour_last) {
                        list_home.add(mDateFormat.format(last.getTime()));
                    }
                    calendars.clear();
                    calendars.add(calendar);
                }
                if (i == length - 1) {
                    Calendar first = calendars.getFirst();
                    int hour_first = first.get(Calendar.HOUR_OF_DAY);
                    if (12 > hour_first) {
                        list_work.add(mDateFormat.format(first.getTime()));
                    }
                    Calendar last = calendars.getLast();
                    int hour_last = last.get(Calendar.HOUR_OF_DAY);
                    if (12 <= hour_last) {
                        list_home.add(mDateFormat.format(last.getTime()));
                    }
                }
            }
            String[] samples1 = null;
            String[] samples2 = null;
            if (0 != list_work.size()) {
                samples1 = new String[list_work.size()];
                samples1 = list_work.toArray(samples1);
            }
            if (0 != list_home.size()) {
                samples2 = new String[list_home.size()];
                samples2 = list_home.toArray(samples2);
            }
            mStatisticsView.drawGraphics(samples1, samples2);
        }
    }
}
