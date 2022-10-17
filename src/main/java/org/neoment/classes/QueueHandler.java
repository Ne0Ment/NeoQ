package org.neoment.classes;

import java.util.ArrayList;
import java.util.List;

public class QueueHandler {
    List<Queue> queues;

    public QueueHandler() {
        this.queues = new ArrayList<Queue>();
    }

    public int getIndexByHome(String check) {
        for (int i=0; i<this.queues.size(); i++) {
            if (this.queues.get(i).checkHome(check)) {
                return i;
            }
        }
        return -1;
    }

    public Queue getQueueByHome(String check) {
        int qIndex = this.getIndexByHome(check);
        if (qIndex!=-1) {
            return this.getQueueByIndex(qIndex);
        } else {
            return null;
        }
    }

    public Queue getQueueByIndex(int index) {
        return this.queues.get(index);
    }

    public void addQueue(Queue q) {
        this.queues.add(q);
    }
}
