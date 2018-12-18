package cn.berfy.sdk.mvpbase.util.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.berfy.sdk.mvpbase.R;
import cn.berfy.sdk.mvpbase.base.CommonActivity;
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter;
import cn.berfy.sdk.mvpbase.util.LogF;

/**
 * author: Berfy
 * date: 2018/12/13
 */
public class MapActivity extends CommonActivity {

//    private MapView mMapView;
    private Bundle mSavedInstanceState;
    public static final String EXTRA_TOKEN = "token";
    private String mToken;
//    private MapboxMap mMapboxMap;
//    private PermissionsManager permissionsManager;

    public static void intoThisActivity(Context context, String token) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra(EXTRA_TOKEN, token);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_map_layout;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mSavedInstanceState = savedInstanceState;
        mToken = getIntent().getStringExtra(EXTRA_TOKEN);
//        Mapbox.getInstance(this, mToken);
    }

    public static class MyHandler extends Handler {

        private WeakReference<Activity> mReference;

        public MyHandler(Activity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null == mReference || null == mReference.get()) {
                return;
            }
            switch (msg.what) {
                case 0:
//                    ((MapActivity) mReference.get()).enableLocationComponent();
                    break;
            }
        }
    }

    @Override
    public void initView() {
//        mMapView = findViewById(R.id.mapView);
//        //地图初始化
//        mMapView.onCreate(mSavedInstanceState);
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(MapboxMap mapboxMap) {
//                mMapboxMap = mapboxMap;
//                new MyHandler(mContext).sendEmptyMessageDelayed(0, 5000);
//            }
//        });
    }

    @SuppressWarnings({"MissingPermission"})
//    private void enableLocationComponent() {
//        // Check if permissions are enabled and if not request
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//
//            LocationComponentOptions options = LocationComponentOptions.builder(this)
//                    .trackingGesturesManagement(true)
//                    .accuracyColor(ContextCompat.getColor(this, R.color.color_138361))
//                    .build();
//
//            // Get an instance of the component
//            LocationComponent locationComponent = mMapboxMap.getLocationComponent();
//
//            // Activate with options
//            locationComponent.activateLocationComponent(this, options);
//
//            // Enable to make component visible
//            locationComponent.setLocationComponentEnabled(true);
//
//            // Set the component's camera mode
//            locationComponent.setCameraMode(CameraMode.TRACKING);
//            locationComponent.setRenderMode(RenderMode.COMPASS);
//        } else {
////            permissionsManager = new PermissionsManager(this);
////            permissionsManager.requestLocationPermissions(this);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, "需要打开您的定位权限，否则您将无法使用定位和导航功能", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            enableLocationComponent();
//        } else {
//            Toast.makeText(this, "需要打开您的定位权限，否则您将无法使用定位和导航功能", Toast.LENGTH_LONG).show();
//            finish();
//        }
//    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
//        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        mMapView.onSaveInstanceState(outState);
    }
}
