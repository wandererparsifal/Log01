package com.parsifal.log01.bean;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;

import java.util.Date;

/**
 * Created by YangMing on 2016/8/24 16:23.
 */
public class TimeAndPlace {

    public Date date;

    public LatLonPoint latLonPoint;

    public TimeAndPlace(Date date, LatLonPoint latLonPoint) {
        this.date = date;
        this.latLonPoint = latLonPoint;
    }
}
