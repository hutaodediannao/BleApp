package com.example.bleapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //获取系统蓝牙适配器管理类
    private BluetoothAdapter mBluetoothAdapter;
    private Intent mEnableBtIntent;
    private BluetoothLeScanner mScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.i(TAG, "onCreate: not support bluetooth");
            finish();
            return;
        } else {
            mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        mEnableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        if (!mBluetoothAdapter.isEnabled()) {
            Log.i(TAG, "onCreate: requestPermissions");
            ActivityResultLauncher<String[]> lun = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                AtomicBoolean isAllOk = new AtomicBoolean(true);
                result.forEach((s, aBoolean) -> {
                    if (!aBoolean) {
                        isAllOk.set(false);
                    }
                });
                if (isAllOk.get()) {
                    Log.i(TAG, "onActivityResult: ok");
                    startActivityForResult(mEnableBtIntent, 1);
                } else {
                    Log.i(TAG, "onActivityResult: no");
                }

            });
            lun.launch(new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
        } else {
            Log.i(TAG, "onCreate: else");
            ActivityResultLauncher<String[]> lun = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                AtomicBoolean isAllOk = new AtomicBoolean(true);
                result.forEach((s, aBoolean) -> {
                    if (!aBoolean) {
                        isAllOk.set(false);
                    }
                });
                if (isAllOk.get()) {
                    Log.i(TAG, "onActivityResult: 权限通过, 初始化扫描");
                    initScanTask();
                } else {
                    Log.i(TAG, "onActivityResult: 权限未通过");
                }
            });
            lun.launch(new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
        }
    }

    // 申请打开蓝牙请求的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();
                initScanTask();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "没有蓝牙权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * 开启扫描任务
     */
    @SuppressLint("MissingPermission")
    private void initScanTask() {
        Log.i(TAG, "startScanTask: ");
        List<ScanFilter> filterList = new ArrayList<>();
        ScanFilter filter = new ScanFilter.Builder()
//                .setDeviceName("Bluno")
                .setDeviceAddress("88:33:14:DC:76:26")
                .build();
        filterList.add(filter);
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        mScanner.startScan(filterList, settings, new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                Log.i(TAG, "onScanResult: result: " + result.getDevice().getName() + ", address:" + result.getDevice().getAddress());
            }
        });
    }

    public void startScan(View view) {

    }
}