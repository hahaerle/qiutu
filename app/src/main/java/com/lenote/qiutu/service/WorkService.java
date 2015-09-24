package com.lenote.qiutu.service;

import android.app.IntentService;
import android.content.Intent;

import org.androidannotations.annotations.EIntentService;

/**
 * Created by lenote on 2015/9/22.
 */
@EIntentService
public class WorkService extends IntentService {
    private static final String TAG = "WorkService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public WorkService() {
        super("WorkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
