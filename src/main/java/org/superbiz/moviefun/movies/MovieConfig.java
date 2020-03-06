package org.superbiz.moviefun.movies;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.superbiz.moviefun.DatabaseServiceCredentials;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class MovieConfig {

    @Bean
    public DataSource moviesDataSource(DatabaseServiceCredentials serviceCredentials) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("movies-mysql"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean moviesLocalContainerEntityManagerFactoryBean(DataSource moviesDataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean moviesLocalContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        moviesLocalContainerEntityManagerFactoryBean.setDataSource(moviesDataSource);
        moviesLocalContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        moviesLocalContainerEntityManagerFactoryBean.setPackagesToScan(this.getClass().getPackage().getName());
        moviesLocalContainerEntityManagerFactoryBean.setPersistenceUnitName("movies");
        return moviesLocalContainerEntityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager moviesPlatformTransactionManager(EntityManagerFactory moviesLocalContainerEntityManagerFactoryBean){
        PlatformTransactionManager moviesPlatformTransactionManager = new JpaTransactionManager(moviesLocalContainerEntityManagerFactoryBean);
        return moviesPlatformTransactionManager;
    }

}
