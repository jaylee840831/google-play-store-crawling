package com.example.securityapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

@TargetApi(4)
/* loaded from: classes2.dex */
public class MyService extends AccessibilityService {
    private static final String TAG = "Myservice";
    private static final String TASK_LIST_VIEW_CLASS_NAME = "com.android.vending";

    @Override // android.accessibilityservice.AccessibilityService
    @TargetApi(4)
    public void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = 4105;
        info.packageNames = new String[]{TASK_LIST_VIEW_CLASS_NAME};
        info.feedbackType = 1;
        info.notificationTimeout = 2000;
        setServiceInfo(info);
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getClassName().toString().equals("android.support.v7.widget.RecyclerView")) {
            getTextView();
        }
    }

    @TargetApi(16)
    private void getTextView() {
        StringBuilder str = recycle(getRootInActiveWindow(), new StringBuilder());
        Intent serviceIntent = new Intent(this, FloatingViewService.class);
        serviceIntent.putExtra("appInfo", str.toString());
        startService(serviceIntent);
    }

    @TargetApi(14)
    public StringBuilder recycle(AccessibilityNodeInfo info, StringBuilder str) {
        if (info.getChildCount() != 0) {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    recycle(info.getChild(i), str);
                }
            }
        } else if (info.getClassName().equals("android.widget.TextView") && info.getText() != null) {
            Log.e(TAG, info.getText().toString());
            str.append(info.getText().toString() + "\n");
        }
        return str;
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
    }
}
