package com.imooc.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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

    @Subscribe
    public void onSuccessEvent(SuccessEvent event) {
        setImageSrc(R.drawable.ic_happy);
    }

    @Subscribe
    public void onFailureEvent(FailureEvent event) {
        setImageSrc(R.drawable.ic_sad);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onPositingEvent(final PostingEvent event) {
        final String threadInfo = Thread.currentThread().toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPublisherThreadInfo(event.threadInfo);
                setSubscriberThreadInfo(threadInfo);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(MainEvent event) {
        setPublisherThreadInfo(event.threadInfo);
        setSubscriberThreadInfo(Thread.currentThread().toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMainOrderedEvent(MainOrderedEvent event) {
        Log.d(TAG, "onMainOrderedEvent: enter @" + SystemClock.uptimeMillis());
        setPublisherThreadInfo(event.threadInfo);
        setSubscriberThreadInfo(Thread.currentThread().toString());
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onMainOrderedEvent: exit @" + SystemClock.uptimeMillis());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBackgroundEvent(final BackgroundEvent event) {
        final String threadInfo = Thread.currentThread().toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPublisherThreadInfo(event.threadInfo);
                setSubscriberThreadInfo(threadInfo);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onAsyncEvent(final AsyncEvent event) {
        final String threadInfo = Thread.currentThread().toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPublisherThreadInfo(event.threadInfo);
                setSubscriberThreadInfo(threadInfo);
            }
        });
    }
    private void setPublisherThreadInfo(String threadInfo) {
        setTextView(R.id.publisherThreadTextView, threadInfo);
    }

    private void setSubscriberThreadInfo(String threadInfo) {
        setTextView(R.id.subscriberThreadTextView, threadInfo);
    }

    /**
     * Should run in UI thread.
     *
     * @param resId
     * @param text
     */
    private void setTextView(int resId, String text) {
        final TextView textView = (TextView) findViewById(resId);
        textView.setText(text);
        textView.setAlpha(.5f);
        textView.animate().alpha(1).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Subscriber");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display dialog fragment (publisher)
                final PublisherDialogFragment fragment = new PublisherDialogFragment();
                fragment.show(getSupportFragmentManager(), "publisher");
            }
        });
    }

    /**
     * Update image resource
     *
     * @param resId
     */
    private void setImageSrc(int resId) {
        final ImageView imageView = (ImageView) findViewById(R.id.emotionImageView);
        imageView.setImageResource(resId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sticky) {
            final Intent intent = new Intent(this, StickyActivity.class);
            EventBus.getDefault().postSticky(new StickyMessageEvent("sticky-message-content"));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
