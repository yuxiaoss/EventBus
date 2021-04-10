package com.imooc.eventbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class StickyActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onStickyMessageEvent(StickyMessageEvent event) {
        setTitle(event.message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
    }
}
