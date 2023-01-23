package sample.executor;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

public class ExecutorServiceTestOnAllMembers {
    public static void main(String[] args) {
        HazelcastInstance hazelcastInstance = buildCluster(3);
        try {
            Runnable runnable = wrap((hz) -> System.out.println("Hello " +hz.getCluster().getLocalMember()));
            IExecutorService executorService = hazelcastInstance.getExecutorService("default");
            executorService.executeOnAllMembers(runnable);
        } finally {
            Hazelcast.shutdownAll();
        }
    }

    private static Runnable wrap(Java8Runnable runnable) {
        return new Java8RunnableAdapter(runnable);
    }

    private static HazelcastInstance buildCluster(int memberCount) {
        Config config = new Config();
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true).addMember("127.0.0.1");
        networkConfig.getRestApiConfig().setEnabled(true);
        config.getManagementCenterConfig().setConsoleEnabled(true).setDataAccessEnabled(true);
        config.getMetricsConfig().setEnabled(true);
        config.getJetConfig().setEnabled(true);
        HazelcastInstance[] hazelcastInstances = new HazelcastInstance[memberCount];
        for (int i = 0; i < memberCount; i++) {
            hazelcastInstances[i] = Hazelcast.newHazelcastInstance(config);
        }
        return hazelcastInstances[0];
    }
}
