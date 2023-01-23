package sample.executor;

import com.hazelcast.core.HazelcastInstance;

import java.io.Serializable;

public interface Java8Runnable extends Serializable {

     void run(HazelcastInstance hazelcastInstance);
}
