package com.imooc.eventbus;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PublisherDialogFragment extends DialogFragment {
    private static final String TAG = "PublisherDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Publisher");
        final String[] items = {"Success", "Failure",
                "Posting", "Main", "MainOrdered", "Background", "Async"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // success
                        postSuccessEvent();
                        break;
                    case 1:
                        // failure
                        postFailureEvent();
                        break;
                    case 2:
                        // posting mode
                        postPostingEvent();
                        break;
                    case 3:
                        // main thread mode
                        postMainEvent();
                        break;
                    case 4:
                        // main ordered thread mode
                        postMainOrderedEvent();
                        break;
                    case 5:
                        // background thread mode
                        postBackgroundEvent();
                        break;
                    case 6:
                        postAsyncEvent();
                        break;
                }
            }

            private void postMainEvent() {
                if (Math.random() > .5) {
                    new Thread("working-thread") {
                        @Override
                        public void run() {
                            super.run();
                            EventBus.getDefault().post(new MainEvent(Thread.currentThread().toString()));
                        }
                    }.start();
                } else {
                    EventBus.getDefault().post(new MainEvent(Thread.currentThread().toString()));
                }
            }

            private void postPostingEvent() {
                if (Math.random() > .5) {
                    new Thread("posting-002") {
                        @Override
                        public void run() {
                            super.run();
                            EventBus.getDefault().post(new PostingEvent(Thread.currentThread().toString()));
                        }
                    }.start();
                } else {
                    EventBus.getDefault().post(new PostingEvent(Thread.currentThread().toString()));
                }
            }
        });
        return builder.create();
    }

    private void postAsyncEvent() {
        if (Math.random() > .5) {
            final ExecutorService pool = Executors.newFixedThreadPool(1);
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new AsyncEvent(Thread.currentThread().toString()));
                }
            });
            pool.shutdown();
        } else {
            EventBus.getDefault().post(new AsyncEvent(Thread.currentThread().toString()));
        }
    }

    private void postBackgroundEvent() {
        if (Math.random() > .5) {
            final ExecutorService pool = Executors.newFixedThreadPool(1);
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new BackgroundEvent(Thread.currentThread().toString()));
                }
            });
            pool.shutdown();
        } else {
            EventBus.getDefault().post(new BackgroundEvent(Thread.currentThread().toString()));
        }
    }

    private void postMainOrderedEvent() {
        Log.d(TAG, "onClick: before @" + SystemClock.uptimeMillis());
        EventBus.getDefault().post(new MainOrderedEvent(Thread.currentThread().toString()));
        Log.d(TAG, "onClick: after @" + SystemClock.uptimeMillis());
    }

    private void postFailureEvent() {
        EventBus.getDefault().post(new FailureEvent());
    }

    private void postSuccessEvent() {
        EventBus.getDefault().post(new SuccessEvent());
    }
}
