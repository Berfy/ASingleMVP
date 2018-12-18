package cn.berfy.sdk.mvpbase.util.map;

import android.content.Context;

import java.util.Map;

/**
 * author: Berfy
 * date: 2018/12/13
 */
public class MapManager {

    public static MapManager mMapManager;
    private Context mContext;

    synchronized public static MapManager getInstances() {
        if (null == mMapManager) {
            synchronized (MapManager.class) {
                if (null == mMapManager) {
                    mMapManager = new MapManager();
                }
            }
        }
        return mMapManager;
    }

    public MapManager() {
    }
}
