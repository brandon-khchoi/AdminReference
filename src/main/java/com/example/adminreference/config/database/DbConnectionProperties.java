package com.example.adminreference.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DbConnectionProperties {

    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
    private Integer maximumPoolSize;
    private Long maxLifetime;
    private Long connectionTimeout;
    private Long validationTimeout;

    private DataSourceProperties dataSourceProperties;

    @Setter
    @Getter
    @ToString
    public static class DataSourceProperties {
        private String cachePrepStmts;
        private String prepStmtCacheSize;
        private String prepStmtCacheSqlLimit;
        private String useServerPrepStmts;
    }

    public HikariDataSource getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setUsername(username);
        config.setPassword(password);
        config.setJdbcUrl(jdbcUrl);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionTimeout(connectionTimeout);
        config.setValidationTimeout(validationTimeout);

        config.addDataSourceProperty("cachePrepStmts", dataSourceProperties.cachePrepStmts);
        config.addDataSourceProperty("prepStmtCacheSize", dataSourceProperties.prepStmtCacheSize);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", dataSourceProperties.prepStmtCacheSqlLimit);
        config.addDataSourceProperty("useServerPrepStmts", dataSourceProperties.useServerPrepStmts);

        return new HikariDataSource(config);
    }

}
