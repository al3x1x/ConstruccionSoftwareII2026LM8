package app.infrastructure.persistence;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriverClassName;

    @Bean
    public DataSource sqlserverDataSource() {
        return DataSourceBuilder.create()
            .url(dbUrl)
            .username(dbUsername)
            .password(dbPassword)
            .driverClassName(dbDriverClassName)
            .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean sqlserverEntityManagerFactory(DataSource sqlserverDataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(sqlserverDataSource);

        // ← CAMBIO: ahora escanea ambos paquetes
        factory.setPackagesToScan(
            "app.domain.models",
            "app.application.adapter.persistence.sqlserver.entities"
        );

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(false);
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform("org.hibernate.dialect.SQLServerDialect");
        factory.setJpaVendorAdapter(adapter);

        java.util.Properties props = new java.util.Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        props.put("hibernate.ddl-auto", "update");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.show_sql", "false");
        factory.setJpaProperties(props);

        factory.setPersistenceUnitName("sqlserver");
        return factory;
    }

    @Bean
    public PlatformTransactionManager sqlserverTransactionManager(EntityManagerFactory sqlserverEntityManagerFactory) {
        return new JpaTransactionManager(sqlserverEntityManagerFactory);
    }
}