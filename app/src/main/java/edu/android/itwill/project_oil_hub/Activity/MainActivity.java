package edu.android.itwill.project_oil_hub.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.android.itwill.project_oil_hub.R;

import edu.android.itwill.project_oil_hub.Control.GPSInfo;
import edu.android.itwill.project_oil_hub.Control.PermissionUtils;
import edu.android.itwill.project_oil_hub.Control.DownloadUrlTask;
import edu.android.itwill.project_oil_hub.Control.ReadJson;

import edu.android.itwill.project_oil_hub.Fragment.SingleDialog;

import edu.android.itwill.project_oil_hub.Model.AllAvgPrices;
import edu.android.itwill.project_oil_hub.Model.MarkerItem;
import edu.android.itwill.project_oil_hub.Model.Oil_LowTop10;
import edu.android.itwill.project_oil_hub.Model.Result_LowTop10;
import static edu.android.itwill.project_oil_hub.Model.OilConstant.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        SingleDialog.SingleChoiceDlgCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private List<String[]> SiGunGu_list;
    private List<String[]> SiGunGu_Code_list;

    // Log 에 쓰일 태그
    public static final String TAG = "project_oil_hub";
    // 전국 평균가를 저장할 파일 이름
    private static final String FILE_NAME = "avgAllPrice.json";
    // 관심 유종 설정시 필요한 상수
    private static final String DLG_OIL = "dlg";
    private static final String OIL_FILE = "file";
    private static final String KEY_SP = "key_prodcd";

    //
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // 시스템에 있는 현재 날짜 가져오기
    private long now = System.currentTimeMillis();
    private SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
    private String formatDate = sdfNow.format(now);

    // Navigation Drawer 에서 사용하는 위젯
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    // Navigation Header에 위치한 위젯에 접근하기 위한 View 및 TextView 위젯
    private View naviHeaderView;
    private TextView textDate, textGSAvg, textDsAvg, textLPGAvg, textPGSAvg, textLOAvg;

    // JSON Data를 해석하기 위한 SingleTon Type Control
    private ReadJson readJson;

    // 전국 평균 유가를 저장하기 위한 List
    private List<AllAvgPrices> avgPricesList;

    // 구글 지도를 사용하기 위한 Member
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient = null;
    private Marker currentMarker = null;
    private LocationRequest locationRequest;
    private boolean mPermissionDenied = false;
    private AppCompatActivity mActivity;
    private boolean askPermissionOnceAgain = false;
    private boolean mRequestingLocationUpdates = false;
    private boolean mMoveMapByUser = true;
    private boolean mMoveMapByAPI = true;
    private LatLng currentPosition;

    private Result_LowTop10 result_lowTop10;
    private List<LatLng> latLngList;
    private GPSInfo gpsInfo;
    private View marker_root_view;
    private TextView tv_marker;
    private ImageView iv_marker;

    private double lat, lon;

    private String oilType, areaCode;

    public MainActivity() {
        // 주소를 통해 지오코딩을 하여 좌표를 얻기 위한 리스트 초기화
        SiGunGu_list = new ArrayList<>();

        SiGunGu_list.add(SEOUL);
        SiGunGu_list.add(GYEONGGI);
        SiGunGu_list.add(GANGWON);
        SiGunGu_list.add(CHUNGBUK);
        SiGunGu_list.add(CHUNGNAM);
        SiGunGu_list.add(JEONBUK);
        SiGunGu_list.add(JEONNAM);
        SiGunGu_list.add(GYEONGBUK);
        SiGunGu_list.add(GYEONGNAM);
        SiGunGu_list.add(BUSAN);
        SiGunGu_list.add(JEJU);
        SiGunGu_list.add(DAEGU);
        SiGunGu_list.add(INCHEON);
        SiGunGu_list.add(GWANGJU);
        SiGunGu_list.add(DAEJEON);
        SiGunGu_list.add(ULSAN);
        SiGunGu_list.add(SEJONG);

        SiGunGu_Code_list = new ArrayList<>();
        SiGunGu_Code_list.add(SEOUL_CODE);
        SiGunGu_Code_list.add(GYEONGGI_CODE);
        SiGunGu_Code_list.add(GANGWON_CODE);
        SiGunGu_Code_list.add(CHUNGBUK_CODE);
        SiGunGu_Code_list.add(CHUNGNAM_CODE);
        SiGunGu_Code_list.add(JEONBUK_CODE);
        SiGunGu_Code_list.add(JEONNAM_CODE);
        SiGunGu_Code_list.add(GYEONGBUK_CODE);
        SiGunGu_Code_list.add(GYEONGNAM_CODE);
        SiGunGu_Code_list.add(BUSAN_CODE);
        SiGunGu_Code_list.add(JEJU_CODE);
        SiGunGu_Code_list.add(DAEGU_CODE);
        SiGunGu_Code_list.add(INCHEON_CODE);
        SiGunGu_Code_list.add(GWANGJU_CODE);
        SiGunGu_Code_list.add(DAEJEON_CODE);
        SiGunGu_Code_list.add(ULSAN_CODE);
        SiGunGu_Code_list.add(SEJONG_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "현재 날짜 : " + formatDate);

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        createLocationRequset();

        readJson = ReadJson.getInstance();

        latLngList = new ArrayList<>();

        mActivity = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        setCustomMarkerView();

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        gpsInfo = new GPSInfo(this);
        lat = gpsInfo.getLocation().getLatitude();
        lon = gpsInfo.getLocation().getLongitude();

        getProdCd();
        changeAddress();

        doDownloadLowTop10UrlTask(getLowTop10Url());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        naviHeaderView = navigationView.getHeaderView(0);

        textDate = naviHeaderView.findViewById(R.id.textWeek);
        textGSAvg = naviHeaderView.findViewById(R.id.textGSAvg);
        textDsAvg = naviHeaderView.findViewById(R.id.textDsAvg);
        textLPGAvg = naviHeaderView.findViewById(R.id.textLPGAvg);
        textPGSAvg = naviHeaderView.findViewById(R.id.textPGSAvg);
        textLOAvg = naviHeaderView.findViewById(R.id.textLOAvg);

        try {
            InputStream in = openFileInput(FILE_NAME);
            Log.i(TAG, "파일이 존재함");

            String text = readFromFile();
            read_AllAvgPrices_JSONData(text);
            Log.i(MainActivity.TAG, FILE_NAME + " 파일 읽기");

            String tradeDT = spAndUniteString(avgPricesList.get(0).getTradeDT());

            if(tradeDT.equals(formatDate)) { // 파일에 저장된 tradeDT와 시스템 날짜가 일치할 경우
                Log.i(MainActivity.TAG, "tradeDT와 시스템 날짜 일치");
                initNaviHeader();
            } else { // 파일에 저장된 tradeDT와 시스템 날짜가 일치하지 않을 경우
                Log.i(MainActivity.TAG, "tradeDT와 시스템 날짜 불일치");
                doDownloadAVGUrlTask(getLocalAvgURL());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(MainActivity.TAG, "파일이 존재하지 않음");
            doDownloadAVGUrlTask(getLocalAvgURL());
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady() 시작");
        mMap = googleMap;

        // 런타임 퍼미션 요청 다이얼로그 혹은 GPS 활성 요청 다이얼로그가 보이기 전에
        // 지도의 초기위치를 서울로 이동
        setDefaultLocation();

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick()");
            }
        });

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (mMoveMapByUser == true && mRequestingLocationUpdates) {
                    Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }

                mMoveMapByUser = true;
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // ActionBar 메뉴 버튼 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SingleDialog dialog = new SingleDialog();
            dialog.show(getSupportFragmentManager(), DLG_OIL);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // NavigationDrawer 메뉴 버튼 클릭 이벤트
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.nav_todayAvgPrice) { // 시/도별 오늘의 평균가
            intent = AvgLocalActivity.newIntent(this);
            startActivity(intent);
        } else if (id == R.id.nav_LW_avg) { // 시/도별 주간 평균가
            intent = AvgLastWeekActivity.newIntent(this);
            startActivity(intent);
        } else if (id == R.id.nav_recent_price_7days) { // 7일간 전국 일일 평균가
            intent = Avg7DaysActivity.newIntent(this);
            startActivity(intent);
        } else if (id == R.id.nav_lowTop_gasStation) { // 지역별 최저가 주유소
            intent = LocalGSActivity.newIntent(this);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createLocationRequset() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setCustomMarkerView() {
        marker_root_view = LayoutInflater.from(this).inflate(R.layout.map_custom_text, null);
        tv_marker = marker_root_view.findViewById(R.id.tv_marker);
        iv_marker = marker_root_view.findViewById(R.id.imageView);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        if (mPermissionDenied) {
            // 퍼미션이 허용되지 않은 경우!
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() == false) {
            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mGoogleApiClient.isConnected()) {
            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }

        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;
                checkPermissions();
            }
        }
    }

    @Override
    protected void onStop() {
        if (mRequestingLocationUpdates) {
            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if (mGoogleApiClient.isConnected()) {
            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mRequestingLocationUpdates == false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mMap.setMyLocationEnabled(true);
                }

            } else {
                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        setDefaultLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentPosition
                = new LatLng(location.getLatitude(), location.getLongitude());

        lat = location.getLatitude();
        lon = location.getLongitude();


        String markerTitle = getCurrentAddress(currentPosition);
        String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                + " 경도:" + String.valueOf(location.getLongitude());

        //현재 위치에 마커 생성하고 이동
        setCurrentLocation(location, markerTitle, markerSnippet);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                && grantResults.length > 0) {
            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {
                if (mGoogleApiClient.isConnected() == false) {
                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }
            } else {
                checkPermissions();
            }
        }

        if (permsRequestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE :
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");

                        if (!mGoogleApiClient.isConnected()) {
                            Log.d(TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }

                        return;
                    }
                }
                break;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private void startLocationUpdates() {
        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }

            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;

            mMap.setMyLocationEnabled(true);
        }

    }

    private void stopLocationUpdates() {

        Log.d(TAG, "stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }

    private boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale) {
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        } else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {


            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if (mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        mMoveMapByUser = false;

        if (currentMarker != null) {
            currentMarker.remove();
        }

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        currentMarker = mMap.addMarker(markerOptions);

        if (mMoveMapByAPI) {

            Log.d(TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                    + location.getLatitude() + " " + location.getLongitude());
            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
            mMap.moveCamera(cameraUpdate);
        }
    }

    // GPS를 잡지 못하였을 경우, 서울을 기본 위치로 설정
    private void setDefaultLocation() {

        mMoveMapByUser = false;

        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }

    private String getCurrentAddress(LatLng latlng) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            return "";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    private String getLowTop10Url() {
        StringBuilder builder = new StringBuilder();

        builder.append(LOW_TOP10_URL).append(OPINET_API_KEY)
                .append(PRODCD_VALUE).append(oilType).append(AREA_VALUE).append(areaCode);

        return builder.toString();
    }

    private String getLocalAvgURL() {
        StringBuilder builder = new StringBuilder();

        builder.append(CON_AVG_URL).append(OPINET_API_KEY);

        return builder.toString();
    }

    private void doDownloadAVGUrlTask(String url) {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {
            Log.i(MainActivity.TAG, "연결된 네트워크 있음");
            DownloadUrlTask task = new DownloadUrlTask() {
                @Override
                protected void onPostExecute(String result) {
                    read_AllAvgPrices_JSONData(result);
                    writeToFile(result);
                    initNaviHeader();
                }
            };

            task.execute(url);

        } else {
            Log.i(MainActivity.TAG, "연결된 네트워크 없음");
        }
    }

    private void doDownloadLowTop10UrlTask(String url) {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {
            Log.i(TAG, "연결된 네트워크 있음");
            DownloadUrlTask task = new DownloadUrlTask() {
                @Override
                protected void onPostExecute(String result) {
                    read_LowTop10_JSONDate(result);

                    convertKATECtoWGS();

                    getMarkerItem();
                }
            };

            task.execute(url);
        } else {
            Log.i(TAG, "연결된 네트워크 없음");
        }

    }

    private void read_AllAvgPrices_JSONData(String text) {
        avgPricesList = readJson.read_AllAvgPrices_JSONData(text);
        Log.i(TAG, "avgPricesList : " + avgPricesList.toString());
    }

    private void read_LowTop10_JSONDate(String text) {
        result_lowTop10 = readJson.read_LowTop10_JsonData(text);
    }

    private void writeToFile(String text) {
        Log.i(TAG, "파일 저장 중");
        try (OutputStream out = openFileOutput(FILE_NAME, MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
             BufferedWriter bw = new BufferedWriter(writer);) {

            bw.write(text);
            Log.i(TAG, FILE_NAME + " 저장 완료");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, FILE_NAME + " 저장 실패");
        }
    }

    private String readFromFile() {
        Log.i(TAG, FILE_NAME + " 읽기 시작");
        StringBuilder builder = new StringBuilder();

        try(InputStream in = openFileInput(FILE_NAME);
            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(reader)) {

            String line = br.readLine();

            while(line != null) {
                builder.append(line);
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, FILE_NAME + "");
        }
        Log.i(TAG, FILE_NAME + " 읽기 완료");
        return builder.toString();
    }

    private void initNaviHeader() {
        Log.i(TAG, "내비게이션 드로어 헤더 TextView 초기화");
        textDate.setText(formatDate);

        for(AllAvgPrices avgPrices : avgPricesList) {
            String avgPrice = convertString(avgPrices);
            Log.i(TAG, "avgPrice : " + avgPrice);

            if (avgPrices.getProdName().equals("휘발유")) {
                textGSAvg.setText(avgPrice);
            }

            if (avgPrices.getProdName().equals("경유")) {
                textDsAvg.setText(avgPrice);
            }

            if (avgPrices.getProdName().equals("LPG")) {
                textLPGAvg.setText(avgPrice);
            }

            if (avgPrices.getProdName().equals("고급휘발유")) {
                textPGSAvg.setText(avgPrice);
            }

            if (avgPrices.getProdName().equals("등유")) {
                textLOAvg.setText(avgPrice);
            }
        }
    }

    private String spAndUniteString(String tDay) {
        String year = tDay.substring(0, 4);
        //Log.i(MainActivity.TAG, "year : " + year);
        String month = tDay.substring(4, 6);
        //Log.i(MainActivity.TAG, "month : " + month);
        String day = tDay.substring(6);
        //Log.i(MainActivity.TAG, "day : " + day);

        StringBuilder builder = new StringBuilder();

        builder.append(year).append("-").append(month).append("-").append(day);

        return builder.toString();
    }

    private String convertString(AllAvgPrices prices) {
        Log.i(TAG, "내비게이션 드로어 헤더에 넣을 Text 변환 시작");
        String vP = prices.getVari_Value();
        // -10.24

        String upDown = "";
        if(vP.substring(0, 1).equals("+")) { // "+"
            upDown = PLUS;
        } else { // "-"
            upDown = MINUS;
        }
        // upDown : ▲ || ▼


        String variation = vP.substring(1);
        // -10.24 -> 10.24
        Log.i(TAG, "variation : " + upDown + variation);

        StringBuilder builder = new StringBuilder();
        builder.append(prices.getProdName()).append("\n") // 유종 이름(휘발유, 경유, ...)
                .append(prices.getAvgPrice()).append("\n") // 유종별 평균가
                .append(upDown).append(variation); // ▲ || ▼ + 10.24

        // "휘발유\n1684.20\n▼6.10"

        return builder.toString();
    }

    @Override
    public void onClickOilTypeSelect(int position) {
        SharedPreferences sp = getSharedPreferences(OIL_FILE, MODE_PRIVATE);
        String prodCode = PRODUCT_CODES[position];

        boolean result = sp.edit().putString(KEY_SP, prodCode).commit();
        if(result) {
            Toast.makeText(this,
                    "설정 변경 결과 : (성공, " + PRODUCTS[position] + ")",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "설정 변경 실패", Toast.LENGTH_SHORT).show();
        }
    }

    private void getProdCd() {
        SharedPreferences sp = getSharedPreferences(OIL_FILE, MODE_PRIVATE);
        oilType = sp.getString(KEY_SP, PRODUCT_CODES[0]);
    }

    private void convertKATECtoWGS() {
        Log.i(TAG, "OIL 객체 내 좌표 변환 시작");
        gpsInfo = new GPSInfo(this);
        List<Address> list = null;
        List<Oil_LowTop10> lowTop10List = result_lowTop10.getList();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (gpsInfo.isGetLocation) {
            try {
                for (int i = 0; i < lowTop10List.size(); i++) {
                    Log.i(TAG, "주소 : " + lowTop10List.get(i).getNewAddress());

                    String address = lowTop10List.get(i).getNewAddress();

                    list = geocoder.getFromLocationName(
                            address,
                            1);
                    Address a = list.get(0);
                    Log.i(TAG, "변환 결과 : " + list.get(0).toString());

                    double lat = a.getLatitude();
                    double lon = a.getLongitude();

                    LatLng latLng = new LatLng(lat, lon);

                    latLngList.add(latLng);

                    Log.i(TAG, (i + 1) + "번째 변환 성공");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "입출력 오류 - 서버에서 주소변환시 에러발생");
            }
        }
        Log.i(TAG, "OIL 객체 내 좌표 변환 종료!");
    }

    private void getMarkerItem() {
        List<MarkerItem> itemList = new ArrayList<>();
        List<Oil_LowTop10> lowTop10List = result_lowTop10.getList();

        for (int i = 0; i < lowTop10List.size(); i++) {

            itemList.add(new MarkerItem(latLngList.get(i).latitude,
                    latLngList.get(i).longitude, lowTop10List.get(i).getPrice(), lowTop10List.get(i).getBrandName()));
        }

        for (MarkerItem markerItem : itemList) {
            addMarker(markerItem);
        }
    }

    private Marker addMarker(MarkerItem markerItem) {
        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        int price = markerItem.getPrice();

        tv_marker.setText(Integer.toString(price));

        for (int i = 0; i < BRANDS.length; i++) {
            if (markerItem.getBrandName().equals(BRANDS[i])) {
                iv_marker.setImageResource(BRANDIMAGES[i]);
            }
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));

        return mMap.addMarker(markerOptions);
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void changeAddress() {
        Log.i(TAG, "OIL 객체 내 좌표 변환 시작");
        gpsInfo = new GPSInfo(this);
        Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());
        Log.i(TAG, "Locale: " + Locale.getDefault());

        List<Address> mResultList = null;

        try {
            Log.i(TAG, "geocoder methods presented");
            mResultList = mGeocoder.getFromLocation(
                    lat,
                    lon,
                    5
            );

            String resultSido = mResultList.get(0).getLocality(); // 시(city)에 대한 정보 출력
            Log.i(TAG, "시/도 추출 결과 : " + resultSido);

            String sido = resultSido.substring(0, 2); // 시에 대한 정보 중 앞 2글자만 추출
            Log.i(TAG, "sido : " + sido);

            String sigungu = mResultList.get(0).getSubLocality(); // 군/구 에 대한 정보 출력
            Log.i(TAG, "시/군/구 추출 결과 : " + sigungu);

            // TODO : 시/군/구 에 대한 code 를 추출하기 위함
            int leng = resultSido.length();
            Log.i(TAG, "resultSido의 길이 : " + leng);
            switch (leng) {
                case 5:
                    for (int i = 0; i < SIDO.length; i++) {
                        if (sido.equals(SIDO[i])) { // 특별시 혹은 광역시 의 경우
                            if (areaCode != null) {
                                break;
                            }
                            String[] siGunGus = SiGunGu_list.get(i); // 해당하는 특별시 혹은 광역시의 배열을 반환
                            Log.i(TAG, "시군구 리스트 추출 : " + siGunGus.toString());
                            for (int j = 0; j < siGunGus.length; j++) {
                                if (sigungu.equals(siGunGus[j])) { // 해당하는 구/군 이 일치할 경우
                                    areaCode = SiGunGu_Code_list.get(i)[j]; // 해당하는 구/군의 코드를 반환
                                    break;
                                }
                            }
                        }
                    }
                    break;

                case 3:
                    String address = mResultList.get(0).getAddressLine(0);
                    String do_NM = splitAndgetString(address)[0];
                    String si_NM = splitAndgetString(address)[1];
                    for (int i = 0; i < SIDO.length; i++) {
                        if (do_NM.equals(SIDO[i])) {
                            if (areaCode != null) {
                                break;
                            }
                            String[] siGunGus = SiGunGu_list.get(i);
                            Log.i(TAG, "시군구 리스트 추출 : " + siGunGus.toString());
                            for (int j = 0; j < siGunGus.length; j++) {
                                if (si_NM.equals(siGunGus[j])) {
                                    areaCode = SiGunGu_Code_list.get(i)[j];
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }
            Log.i(TAG, "시군구 코드 : " + areaCode);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "주소변환 실패");
            Log.e(TAG, e.getMessage());
        }

    }

    private String[] splitAndgetString(String text) {
        String[] addresses = text.split(" ");

        String[] result = {addresses[1], addresses[2]};

        return result;
    }
}
