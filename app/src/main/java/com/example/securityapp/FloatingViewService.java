package com.example.securityapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;

/* loaded from: classes2.dex */
public class FloatingViewService extends Service {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private View mFloatingView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams params;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getExtras() == null) {
            return 1;
        }
        ((TextView) this.mFloatingView.findViewById(R.id.textview)).setText(intent.getStringExtra("appInfo"));
        return 1;
    }

    @Override // android.app.Service
    @SuppressLint({"ClickableViewAccessibility", "WrongConstant"})
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
        this.mFloatingView = LayoutInflater.from(this).inflate(R.layout.overlayout, (ViewGroup) null);
        if (Build.VERSION.SDK_INT >= 26) {
            this.params = new WindowManager.LayoutParams(-2, -2, 2038, 8, -3);
        } else {
            this.params = new WindowManager.LayoutParams(-2, -2, 2002, 8, -3);
        }
        WindowManager.LayoutParams layoutParams = this.params;
        layoutParams.gravity = 51;
        layoutParams.x = 0;
        layoutParams.y = 100;
        this.mWindowManager = (WindowManager) getSystemService("window");
        this.mWindowManager.addView(this.mFloatingView, this.params);
        ((ImageView) this.mFloatingView.findViewById(R.id.close_btn)).setOnClickListener(new View.OnClickListener() { // from class: com.example.securityapp.FloatingViewService.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FloatingViewService.this.stopSelf();
            }
        });
        this.mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() { // from class: com.example.securityapp.FloatingViewService.2
            private float initialTouchX;
            private float initialTouchY;
            private int initialX;
            private int initialY;

            @SuppressLint("WrongConstant")
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == 0) {
                    this.initialX = FloatingViewService.this.params.x;
                    this.initialY = FloatingViewService.this.params.y;
                    this.initialTouchX = event.getRawX();
                    this.initialTouchY = event.getRawY();
                    return true;
                } else if (action == 1) {
                    int Xdiff = (int) (event.getRawX() - this.initialTouchX);
                    int Ydiff = (int) (event.getRawY() - this.initialTouchY);
                    if (Xdiff < 10 && Ydiff < 10) {
                        Intent intent = new Intent(FloatingViewService.this.getApplicationContext(), MainActivity.class);
                        intent.setFlags(268435456);
                        intent.putExtra("fromwhere", "ser");
                        FloatingViewService.this.startActivity(intent);
                    }
                    return true;
                } else if (action != 2) {
                    return false;
                } else {
                    FloatingViewService.this.params.x = this.initialX + ((int) (event.getRawX() - this.initialTouchX));
                    FloatingViewService.this.params.y = this.initialY + ((int) (event.getRawY() - this.initialTouchY));
                    FloatingViewService.this.mWindowManager.updateViewLayout(FloatingViewService.this.mFloatingView, FloatingViewService.this.params);
                    return true;
                }
            }
        });
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        View view = this.mFloatingView;
        if (view != null) {
            this.mWindowManager.removeView(view);
        }
    }

    @SuppressLint("WrongConstant")
    private void startMyOwnForeground() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel chan = new NotificationChannel(BuildConfig.APPLICATION_ID, "My Background Service", 0);
            chan.setLightColor(-16776961);
            chan.setLockscreenVisibility(0);
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(chan);
            startForeground(2, new NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID).setOngoing(true).setContentTitle("App is running in background").setPriority(1).setCategory(NotificationCompat.CATEGORY_SERVICE).build());
        }
    }
}