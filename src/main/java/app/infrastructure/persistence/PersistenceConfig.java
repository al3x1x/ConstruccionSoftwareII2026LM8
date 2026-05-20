package app.infrastructure.persistence;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
    basePackages = "app.application.adapter.persistence.sqlserver.repositories",
    entityManagerFactoryRef = "sqlserverEntityManagerFactory",
    transactionManagerRef = "sqlserverTransactionManager"
)
@EnableMongoRepositories(
    basePackages = "app.application.adapter.persistence.mongodb.repositories"
)
public class PersistenceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource sqlserverDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean sqlserverEntityManagerFactory(DataSource sqlserverDataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(sqlserverDataSource);
        factory.setPackagesToScan("app.application.adapter.persistence.sqlserver.entities");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPersistenceUnitName("sqlserver");
        return factory;
    }

    @Bean
    public PlatformTransactionManager sqlserverTransactionManager(EntityManagerFactory sqlserverEntityManagerFactory) {
        return new JpaTransactionManager(sqlserverEntityManagerFactory);
    }
}
