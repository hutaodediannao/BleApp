package com.example.bleapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //获取系统蓝牙适配器管理类
    private BluetoothAdapter mBluetoothAdapter;
    private Intent mEnableBtIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.i(TAG, "onCreate: not support bluetooth");
            finish();
            return;
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
            lun.launch(new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN});
        } else {
            Log.i(TAG, "onCreate: else");
            ActivityResultLauncher<String> lun = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
                if (result) {
                    Log.i(TAG, "onActivityResult: 权限通过, 初始化扫描任务...");
                    // 开启扫描
                    startScanTask();
                } else {
                    Log.i(TAG, "onActivityResult: 权限未通过");
                }
            });
            lun.launch(Manifest.permission.BLUETOOTH_CONNECT);
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
                startScanTask();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "没有蓝牙权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * 开启扫描任务
     */
    private void startScanTask(){
        Log.i(TAG, "startScanTask: ");



    }

}