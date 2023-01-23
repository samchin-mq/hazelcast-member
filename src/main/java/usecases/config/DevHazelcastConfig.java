package usecases.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
public class DevHazelcastConfig {

    @Bean
    HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance(config());
    }

    @Bean
    Config config() {

        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true).addMember("127.0.0.1");
        networkConfig.getRestApiConfig().setEnabled(true);
        Config config = new Config();
        config.setClusterName("hazelcast-dev");
        config.setNetworkConfig(networkConfig);
        config.getManagementCenterConfig().setConsoleEnabled(true).setDataAccessEnabled(true);
        config.getMetricsConfig().setEnabled(true);
        config.getJetConfig().setEnabled(true);
        //Enterprise
//        PersistenceConfig persistenceConfig = new PersistenceConfig().setEnabled(true);
//        config.setPersistenceConfig(persistenceConfig);
        return config;
    }
}
