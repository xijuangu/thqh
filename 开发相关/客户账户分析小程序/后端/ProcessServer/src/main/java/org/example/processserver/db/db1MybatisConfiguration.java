package org.example.processserver.db;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author PY.Lu
 * @date 2024/10/30
 * @Description
 */
@Configuration
@MapperScan(basePackages = "org.example.processserver.mapper.db1", sqlSessionFactoryRef = "db1SqlSessionFactory")
public class db1MybatisConfiguration {

    @Bean(name = "db1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource getDataSource() {
        System.out.println("db1DataSource init success");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db1SqlSessionFactory")
    public SqlSessionFactory SqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        String mapperLocation = "classpath*:mapper/*.xml";
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperLocation));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "db1SqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate(@Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    @Bean(name = "db1TransactionManager")
//    @Primary
//    public DataSourceTransactionManager getTransactionManager(@Qualifier("db1DataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
}
