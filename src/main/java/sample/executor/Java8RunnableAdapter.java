package sample.executor;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;

import java.io.Serializable;

public class Java8RunnableAdapter implements Runnable, Serializable, HazelcastInstanceAware {
    private Java8Runnable runnable;
    private transient HazelcastInstance hazelcastInstance;

    // Deserialization constructor
    public Java8RunnableAdapter() {
    }

    public Java8RunnableAdapter(Java8Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run(hazelcastInstance);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
