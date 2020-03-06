package org.superbiz.moviefun.albums;

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
public class AlbumConfig {

    @Bean
    public DataSource albumsDataSource(DatabaseServiceCredentials serviceCredentials) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("albums-mysql"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean albumsLocalContainerEntityManagerFactoryBean(DataSource albumsDataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean albumsLocalContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        albumsLocalContainerEntityManagerFactoryBean.setDataSource(albumsDataSource);
        albumsLocalContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        albumsLocalContainerEntityManagerFactoryBean.setPackagesToScan(this.getClass().getPackage().getName());
        albumsLocalContainerEntityManagerFactoryBean.setPersistenceUnitName("albums");
        return albumsLocalContainerEntityManagerFactoryBean;
    }
    @Bean
    public PlatformTransactionManager albumsPlatformTransactionManager(EntityManagerFactory albumsLocalContainerEntityManagerFactoryBean){
        PlatformTransactionManager albumsPlatformTransactionManager = new JpaTransactionManager(albumsLocalContainerEntityManagerFactoryBean);
        return albumsPlatformTransactionManager;
    }
}
