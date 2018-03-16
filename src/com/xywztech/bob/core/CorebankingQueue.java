package com.xywztech.bob.core;

import java.util.ArrayList;
import java.util.List;

public class CorebankingQueue {
    
    private static CorebankingQueue instance;
                  
    private List<CorebankingQueueItem> queueList;

    private CorebankingQuery queryThread;
    
    private CorebankingQueue() {
        // Exists only to defeat instantiation.   
        queueList = new ArrayList<CorebankingQueueItem>();
    }
    
    public static synchronized CorebankingQueue getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new CorebankingQueue();
        }
        return instance;
    }
    
    public synchronized int addQueueItem(Long id, String account, String userId, String authUnits) {
        CorebankingQueueItem item = new CorebankingQueueItem(id, account, userId, authUnits);
        queueList.add(item);
        queryThread.resume();
        return queueList.size();
    }

    public synchronized void removeQueueItem(CorebankingQueueItem item) {
        if (queueList.contains(item)) {
            queueList.remove(item);
        }
    }

    public synchronized void moveQueueItem(CorebankingQueueItem item) {
        if (queueList.contains(item)) {
            queueList.remove(item);
            queueList.add(item);
        }
    }
    
    public synchronized void startQueryThread() {
        if (queryThread == null) {
            queryThread = new CorebankingQuery();
        }
        Thread t = new Thread(queryThread);
        t.start();
    }
    
    public synchronized void stopQueryThread() {
        if (queryThread != null) {
            queryThread.stop();
        }
        queryThread = null;
    }
    
    public synchronized CorebankingQueueItem getFirstQueueItem() {
        if (queueList.isEmpty()) {
            return null;
        } else {
            return queueList.get(0);
        }
    }
    
}