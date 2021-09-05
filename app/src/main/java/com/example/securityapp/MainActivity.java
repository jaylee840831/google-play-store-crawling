package com.example.securityapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(this)) {
            ((Button) findViewById(R.id.open_float)).setEnabled(false);
        }
    }

    public void open_float(View view) {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 1);
        }
    }

    @SuppressLint("WrongConstant")
    public void open_security(View view) {
        Toast.makeText(this, "開啟協助工具並且找到 SecurityApp", 0).show();
        startActivity(new Intent("android.settings.SETTINGS"));
    }

    @SuppressLint("WrongConstant")
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "顯示於上方的權限授權成功", 1).show();
            } else {
                Toast.makeText(this, "請先到設定->應用程式->SecurityApp 授予顯示於上方的權限", 1).show();
            }
        }
    }
}
