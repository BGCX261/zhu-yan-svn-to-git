package com.cc.software.calendar.helper;

public interface IAsyncQueryJob {
    /**
     * this is the hard work, usually done in another thread than the ui thread
     * 
     * @return
     */
    public Object doAsyncJob(Object jobArg);

    /**
     * this is the processing part, result is the result returned in doJob. this
     * is usually done in the ui thread.
     * 
     * @param result
     */
    public void asyncJobDone(Object result, Object jobArg);
}
