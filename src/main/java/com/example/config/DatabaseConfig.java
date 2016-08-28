package com.example.config;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by bijoy on 17/8/16.
 */
@Configuration
@EnableTransactionManagement
@EntityScan(value = "com.example.domain")
public class DatabaseConfig {

    @Bean
    public SessionFactory sessionFactory(HibernateEntityManagerFactory factory){
        return factory.getSessionFactory();
    }
}
