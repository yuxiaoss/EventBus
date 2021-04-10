package com.imooc.eventbus;

public class PostingEvent {
    public final String threadInfo;

    public PostingEvent(String threadInfo) {
        this.threadInfo = threadInfo;
    }
}
