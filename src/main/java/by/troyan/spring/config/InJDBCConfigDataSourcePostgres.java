package by.troyan.spring.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {
        "by.troyan.aspect",
        "by.troyan.entity",
        "by.troyan.repository",
        "by.troyan.strategy"})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class InJDBCConfigDataSourcePostgres {

    private static final Logger LOG = LogManager.getLogger(InJDBCConfigDataSourcePostgres.class);

    @Value(value = "src/main/resources/jdbc.properties")
    private String filename;

    @Bean
    public Properties propertiesFromFile(){
        FileInputStream fileInputStream;
        Properties property = new Properties();
        try {
            fileInputStream = new FileInputStream(filename);
            property.load(fileInputStream);
            return property;
        } catch (IOException e) {
            LOG.fatal("Can not read properties " + e);
            throw new RuntimeException();
        }
    }

    @Bean
    public DataSource driverManagerDataSourcePostgres(){
        Properties jdbcProperties = propertiesFromFile();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcProperties.getProperty("jdbc.url"));
        dataSource.setUsername(jdbcProperties.getProperty("jdbc.username"));
        dataSource.setPassword(jdbcProperties.getProperty("jdbc.password"));

        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(driverManagerDataSourcePostgres());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(driverManagerDataSourcePostgres());
    }

}
