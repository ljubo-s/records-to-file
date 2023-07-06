package recordstofile.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DbConfig {

    @Value("${spring.datasource.jdbc.template.fetch-size}")
    int fetchSize;

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = "NamedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource());
        namedParameterJdbcTemplate.getJdbcTemplate().setFetchSize(fetchSize);
        return namedParameterJdbcTemplate;
    }

}
