//TODO: this package should be renamed to async or something like this, in phone & pad code.

package com.cc.software.calendar.helper;

import android.content.AsyncQueryHandler;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class AsyncJobHandler extends Handler {
    public final static AsyncJobHandler getInstance() {
        synchronized (AsyncJobHandler.class) {
            if (instance == null) {
                instance = new AsyncJobHandler();
            }
        }
        return instance;
    }

    private Looper mLooper = null;
    private Handler mWorkHandler = null;
    private static AsyncJobHandler instance;

    public synchronized void release() {
        destroy();
    }

    private void destroy() {
        mWorkHandler.removeMessages(1);
        mLooper.quit();
        instance = null;
        mWorkHandler = null;
        mLooper = null;
    }

    /*
     * initiate this in the ui thread so you can do ui works in
     *
     * @IAsyncQueryJob.jobDone
     */
    private AsyncJobHandler() {
        synchronized (AsyncQueryHandler.class) {
            if (mLooper == null) {
                HandlerThread thread = new HandlerThread("AsyncJobHandler");
                thread.start();
                mLooper = thread.getLooper();
            }
        }
        mWorkHandler = new WorkHandler(mLooper);
    }

    @Override
    public void handleMessage(Message msg) {
        WorkArg arg = (WorkArg) msg.obj;
        try {
            arg.job.asyncJobDone(arg.result, arg.arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * At a given time there can be only one job, anything else will be canceled
     * TODO: double check me, whether or not can do this
     * @param job
     */
    public void startJob(IAsyncQueryJob job, Object jobArg) {
        Message message = mWorkHandler.obtainMessage(1);
        message.obj = new WorkArg(job, this, jobArg);
        message.sendToTarget();
    }

    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        /**
         * this is done in my worker thread
         */
        @Override
        public void handleMessage(Message msg) {
            WorkArg workArg = (WorkArg) msg.obj;
            Object result = null;
            try {
                result = workArg.job.doAsyncJob(workArg.arg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message message = workArg.handler.obtainMessage(1);
            workArg.result = result;
            message.obj = workArg;
            message.sendToTarget();
        }
    }

    private class WorkArg {
        WorkArg(IAsyncQueryJob j, Handler h, Object arg) {
            job = j;
            handler = h;
            this.arg = arg;
        }

        IAsyncQueryJob job;
        Handler handler;
        Object result;
        Object arg;
    }
}