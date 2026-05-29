package com.minipos.pos;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

// Отключаем автоматическую настройку DataSource и Hibernate со стороны Spring Boot
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class MiniposApplication {
    // Теперь класс чист, не конфликтует с нашим JDBC и используется только как точка сборки
}