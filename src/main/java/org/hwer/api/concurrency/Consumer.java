package org.hwer.api.concurrency;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * @class Consumer
 * @brief Implements a consumer of tasks that make use of the engine
 *        The use of the consumer is to guarantee that only a single task will use the engine at
 *        any time.
 */
public class Consumer implements Runnable {
    /**
     * @brief Constructor
     *
     * @param queue
     *     The queue of tasks
     */
    public Consumer (BlockingQueue queue) {
        queue_ = queue;

        running_ = true;
    }

    /**
     * @brief Runnable's run method
     */
    @Override
    public void run () {
        while (running_) {
            try {
                Runnable task = (Runnable) (queue_.poll(1, TimeUnit.SECONDS));

                if (task != null) {
                    task.run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @brief Getter method for whether this Consumer is running
     *
     * @return True if this Consumer is running
     */
    public boolean isRunning () {
        return running_;
    }

    /**
     * @brief Setter method for the running status of this Consumer
     *
     * @param running
     *     The new running status of this Consumer
     */
    public void setRunning (boolean running) {
        running_ = running;
    }

    private final BlockingQueue queue_; //!< The queue of tasks

    private boolean running_; //!< Flag that indicates whether this Consumer should be running

}
