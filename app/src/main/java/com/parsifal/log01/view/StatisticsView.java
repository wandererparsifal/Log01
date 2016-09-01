package com.parsifal.log01.view;

import com.parsifal.log01.ui.view.StatisticsData;

/**
 * Created by YangMing on 2016/8/24 15:59.
 */
public interface StatisticsView extends BaseView {
    void drawGraphics(StatisticsData data1, StatisticsData data2);
}
