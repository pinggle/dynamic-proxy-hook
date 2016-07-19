package com.weishu.upf.dynamic_proxy_hook.app2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.weishu.upf.dynamic_proxy_hook.app2.hook.EvilInstrumentation;
import com.weishu.upf.dynamic_proxy_hook.app2.hook.HookHelper;
import com.weishu.upf.dynamic_proxy_hook.app2.ui.InterfaceView;

import java.lang.reflect.Field;

/**
 * @author weishu
 * @date 16/1/28
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("DTPrint onCreate --- ");

        // HOOK (Activity::startActivity);
        try {
            HookHelper.attachActivity(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InterfaceView _tmpInterfaceView = new InterfaceView(MainActivity.this, this.getApplicationContext());
        setContentView(_tmpInterfaceView);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        System.out.println("DTPrint attachBaseContext --- ");
        try {
            // HOOK (ContextImpl::startActivity);
            HookHelper.attachContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
