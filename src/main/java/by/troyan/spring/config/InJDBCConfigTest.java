package by.troyan.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {
        "by.troyan.aspect",
        "by.troyan.entity",
        "by.troyan.repository",
        "by.troyan.strategy.jdbc.impl"})
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class InJDBCConfigTest {

    @Bean
    public DataSource driverManagerDataSource(){
        return  new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("initdb/postgresql/schema.sql")
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
        return new NamedParameterJdbcTemplate(driverManagerDataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(driverManagerDataSource());
    }
}
