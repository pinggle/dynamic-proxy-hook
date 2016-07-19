package com.weishu.upf.dynamic_proxy_hook.app2.ui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 动态创建一个View;
 * Created by yanping on 16/7/19.
 */
public class InterfaceView extends RelativeLayout {

    private Context mContext;
    private Context mAppContext;

    public InterfaceView(Context context, Context appContext) {
        super(context);
        mContext = context;
        mAppContext = appContext;
        initView();
    }

    private void initView() {

        TextView tvAttachContext = new TextView(mContext);
        tvAttachContext.setText("测试-AttachContext");
        tvAttachContext.setTextColor(Color.rgb(52, 171, 117));
        tvAttachContext.setBackgroundColor(Color.WHITE);
        tvAttachContext.setPadding(30, 30, 80, 30);
        tvAttachContext.setTextSize(14f);
        LayoutParams sAttachContextParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sAttachContextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        sAttachContextParams.setMargins(50, 50, 0, 0);
        addView(tvAttachContext, sAttachContextParams);
        tvAttachContext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("http://www.baidu.com"));

                // 注意这里使用的ApplicationContext 启动的Activity
                // 因为Activity对象的startActivity使用的并不是ContextImpl的mInstrumentation
                // 而是自己的mInstrumentation, 如果你需要这样, 可以自己Hook
                // 比较简单, 直接替换这个Activity的此字段即可.
                mContext.startActivity(intent); // 调用 Activity::startActivity ;
            }
        });

        TextView tvAttachActivity = new TextView(mContext);
        tvAttachActivity.setText("测试-AttachActivity");
        tvAttachActivity.setTextColor(Color.rgb(52, 171, 117));
        tvAttachActivity.setBackgroundColor(Color.WHITE);
        tvAttachActivity.setPadding(30, 30, 80, 30);
        tvAttachActivity.setTextSize(14f);
        LayoutParams sAttachActivityParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sAttachActivityParams.setMargins(50, 300 + 50, 0, 0);
        sAttachContextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(tvAttachActivity, sAttachActivityParams);
        tvAttachActivity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("http://www.hao123.com"));
                mAppContext.startActivity(intent);  // 调用 ContextImpl::startActivity ;
            }
        });

    }
}
