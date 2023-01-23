package usecases.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!local")
@Configuration
public class HazelcastConfig {

    @Bean
    HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance(config());
    }

    @Bean
    Config config() {
        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.getJoin().getKubernetesConfig().setEnabled(true);
        Config config = new Config();
        config.setClusterName("hazelcast-dev");
        config.setNetworkConfig(networkConfig);
        config.getManagementCenterConfig().setConsoleEnabled(true).setDataAccessEnabled(true);
        config.getMetricsConfig().setEnabled(true);
        config.getJetConfig().setEnabled(true);
        return config;
    }
}
