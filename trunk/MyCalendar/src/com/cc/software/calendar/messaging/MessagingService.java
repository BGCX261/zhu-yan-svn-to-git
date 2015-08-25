package com.cc.software.calendar.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

public class MessagingService {
    private static MessagingService instance = null;

    public final static MessagingService getInstance() {
        synchronized (MessagingService.class) {
            if (instance == null) {
                instance = new MessagingService();
            }
        }
        return instance;
    }

    HashMap<Integer, ArrayList<Callback>> listenerMap;

    private MessagingService() {
        listenerMap = new HashMap<Integer, ArrayList<Callback>>(10);
    }

    public synchronized void register(Callback listener, final int... message) {
        for (int i = 0, len = message.length; i < len; i++) {
            final Integer msgId = Integer.valueOf(message[i]);
            ArrayList<Callback> listeners = listenerMap.get(msgId);
            if (listeners == null) {
                listeners = new ArrayList<Handler.Callback>(5);
                listenerMap.put(msgId, listeners);
                listeners.add(listener);
            } else {
                if (listeners.contains(listener) == false) {
                    listeners.add(listener);
                }
            }
        }
    }

    public synchronized void unregister(Callback listener) {
        Iterator<ArrayList<Callback>> listeners = listenerMap.values().iterator();
        while (listeners.hasNext())
            listeners.next().remove(listener);
    }

    /**
     * msg will be recycled after this call, don't use it anymore.
     * @param msg
     */
    public synchronized void publish(Message msg) {
        publish(msg, true);
    }

    /**
     * handy method to sync a message only has a message id
     * @param messageId
     */
    public synchronized void publish(int messageId) {
        Message message = Message.obtain(null, messageId);
        publish(message, true);
    }

    public synchronized void publish(Message msg, boolean recycle) {
        final int msgId = Integer.valueOf(msg.what);
        ArrayList<Callback> listeners = listenerMap.get(msgId);
        if (listeners == null) {
            return;
        } else {
            for (int i = 0, len = listeners.size(); i < len; i++) {
                listeners.get(i).handleMessage(msg);
            }
        }
        if (recycle)
            msg.recycle();
    }
}
