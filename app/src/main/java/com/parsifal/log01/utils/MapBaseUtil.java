package com.parsifal.log01.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.RouteSearch;

import java.util.List;

/**
 * Created by SE1 on 2016/5/25.
 */
public class MapBaseUtil {

    private static final String TAG = MapBaseUtil.class.getSimpleName();

    private Context mContext = null;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationListener mAMapLocationListener = null;
    private AMapLocationListener mOutsideLocationListener = null;

    private PoiSearch.Query mQuery = null;
    private PoiSearch.SearchBound mBound = null;
    private PoiSearch mPoiSearch = null;

    private boolean isInitialized = false;

    public void init(Context context) {
        initLocation(context);
        initPoiSearch(context);
        mContext = context;
        isInitialized = true;
    }

    public void initLocation(Context context) {
        mLocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setGpsFirst(true);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (null != mOutsideLocationListener) {
                    mOutsideLocationListener.onLocationChanged(aMapLocation);
                }
            }
        };
        mLocationClient.setLocationListener(mAMapLocationListener);
    }

    private void initPoiSearch(Context context) {
        mPoiSearch = new PoiSearch(context, null);
    }

    public void locate(AMapLocationListener aMapLocationListener) {
        if (isInitialized) {
            mOutsideLocationListener = aMapLocationListener;
            mLocationClient.startLocation();
        } else {
            LogUtil.w(TAG, "MapUtil is uninitialized.");
        }
    }

    public void poiSearch(String query, String ctgr, String city, int currentPage, int pageSize,
                          PoiSearch.OnPoiSearchListener onPoiSearchListener) {
        if (isInitialized) {
            mQuery = new PoiSearch.Query(query, ctgr, city);
            mQuery.setPageSize(pageSize);
            mQuery.setPageNum(currentPage);
            mPoiSearch.setQuery(mQuery);
            mPoiSearch.setOnPoiSearchListener(onPoiSearchListener);
            mPoiSearch.searchPOIAsyn();
        } else {
            LogUtil.w(TAG, "MapUtil is uninitialized.");
        }
    }

    public void poiBoundSearch(String query, String ctgr, String city, LatLonPoint point,
                               int meters, int currentPage, int pageSize, PoiSearch.OnPoiSearchListener onPoiSearchListener) {
        if (isInitialized) {
            mQuery = new PoiSearch.Query(query, ctgr, city);
            mQuery.setPageSize(pageSize);
            mQuery.setPageNum(currentPage);
            mPoiSearch.setQuery(mQuery);

            mBound = new PoiSearch.SearchBound(point, meters, true);
            mPoiSearch.setBound(mBound);
            mPoiSearch.setOnPoiSearchListener(onPoiSearchListener);
            mPoiSearch.searchPOIAsyn();
        } else {
            LogUtil.w(TAG, "MapUtil is uninitialized.");
        }
    }

    public PoiSearch.Query getQuery() {
        return mQuery;
    }

    public void routeSearch(RouteSearch.FromAndTo fromAndTo, int mode, List<LatLonPoint> passedByPoints,
                            List<List<LatLonPoint>> avoidpolygons, String avoidRoad, RouteSearch.OnRouteSearchListener onRouteSearchListener) {
        if (isInitialized) {
            RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(fromAndTo,
                    mode, passedByPoints, avoidpolygons, avoidRoad);
            RouteSearch routeSearch = new RouteSearch(mContext);
            routeSearch.setRouteSearchListener(onRouteSearchListener);
            routeSearch.calculateDriveRouteAsyn(driveRouteQuery);
        } else {
            LogUtil.w(TAG, "MapUtil is uninitialized.");
        }
    }

    public void release() {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }
}
