package com.jhipster.bachelor.inventory.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.jhipster.bachelor.inventory.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration {

  private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

  public DatabaseConfiguration() {
  }

  /**
   * Open the TCP port for the H2 database, so it is available remotely.
   *
   * @return the H2 database TCP server
   * @throws SQLException if the server failed to start
   */
  //  @Bean(initMethod = "start", destroyMethod = "stop")
  //  @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
  //  public Object h2TCPServer() throws SQLException {
  //    //String port = "1" + env.getProperty("server.port");
  //    String port = "9090";
  //    log.debug("H2 database is available on port {}", port);
  //    return H2ConfigurationHelper.createServer(port);
  //  }
}
