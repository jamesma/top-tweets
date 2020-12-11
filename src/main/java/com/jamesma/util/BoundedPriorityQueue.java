package com.jamesma.util;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;

@Slf4j
public class BoundedPriorityQueue<T extends Comparable<T>> {

    final PriorityQueue<T> priorityQueue;
    final int maxSize;

    public BoundedPriorityQueue(int maxSize) {
        Preconditions.checkArgument(maxSize > 0);
        this.maxSize = maxSize;
        this.priorityQueue = new PriorityQueue<>();
    }

    public void offer(T item) {
        if (priorityQueue.size() < maxSize) {
            priorityQueue.offer(item); // O(log(n))
            log.info("PQ below maxSize, Added {}", item);
        } else {
            T min = priorityQueue.peek();
            if (min != null) {
                int result = item.compareTo(min);
                if (result > 0) {
                    T removed = priorityQueue.poll();
                    priorityQueue.offer(item); // O(log(n))
                    log.info("Removed {}, Added {}", removed, item);
                }
            }
        }
    }

    public boolean contains(T item) {
        // linear time
        return priorityQueue.contains(item);
    }

    public Object[] toArray() {
        return priorityQueue.toArray();
    }
}
