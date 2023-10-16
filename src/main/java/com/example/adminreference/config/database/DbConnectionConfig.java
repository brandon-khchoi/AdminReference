package com.example.adminreference.config.database;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = {"com.example.adminreference.repository"},
        entityManagerFactoryRef = "dbAdminEntityManager",
        transactionManagerRef = "dbAdminTransactionManager"
)
@EnableTransactionManagement
@Slf4j
public class DbConnectionConfig {

    @Value("#{'${spring.jpa.show-sql:false}'}")
    private boolean showSql;

    @Bean("dbCellookReadProperties")
    @ConfigurationProperties(prefix = "spring.dbadmin.read.datasource.hikari")
    public DbConnectionProperties dbAdminReadProperties() {
        return new DbConnectionProperties();
    }

    @Primary
    @Bean("dbCellookWriteProperties")
    @ConfigurationProperties(prefix = "spring.dbadmin.write.datasource.hikari")
    public DbConnectionProperties dbAdminWriteProperties() {
        return new DbConnectionProperties();
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory(@Qualifier("dbAdminEntityManager") EntityManager dbAdminEntityManager) {
        return new JPAQueryFactory(dbAdminEntityManager);
    }

    @Primary
    @Bean(name = "dbAdminEntityManager")
    public LocalContainerEntityManagerFactoryBean dbAdminEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dbAdminRoutingDataSource());
        em.setPackagesToScan("com.example.adminreference.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(showSql);
//        vendorAdapter.setDatabasePlatform("com.example.adminreference.config.dbconfig.MySqlCustomDialect");
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.physical_naming_strategy", CustomJpaNamingStrategy.class.getName());
        jpaProperties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        em.setJpaPropertyMap(jpaProperties);
        return em;
    }

    @Primary
    @Bean("dbAdminRoutingDataSource")
    public DataSource dbAdminRoutingDataSource() {
        HikariDataSource masterDataSource = dbAdminWriteProperties().getHikariConfig();
        HikariDataSource slaveDataSource = dbAdminReadProperties().getHikariConfig();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("MASTER", masterDataSource);
        dataSourceMap.put("SLAVE", slaveDataSource);
        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        return routingDataSource;
    }

    @Primary
    @Bean("routingLazyDataSource")
    public DataSource routingLazyDataSource(@Qualifier("dbAdminRoutingDataSource") DataSource dataSource) {
        return new LazyConnectionDataSourceProxy(dataSource);
    }

    @Primary
    @Bean(name = "dbAdminTransactionManager")
    public PlatformTransactionManager dbAdminTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(dbAdminEntityManager().getObject());
        return transactionManager;
    }

    @Bean("dbAdminDataSourceInitializer")
    public DataSourceInitializer dbAdminDataSourceInitializer(@Qualifier("dbAdminRoutingDataSource") DataSource datasource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("h2/schema.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("h2/data.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(datasource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }

}
