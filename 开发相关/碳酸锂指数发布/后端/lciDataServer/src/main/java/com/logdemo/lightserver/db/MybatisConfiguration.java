package com.logdemo.lightserver.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author PY.Lu
 * @date 2025/3/6
 * @Description
 */

/**
 * MyBatis核心配置类
 * 功能：配置数据源、SqlSessionFactory、SqlSessionTemplate等MyBatis核心组件，
 * 完成MyBatis与Spring的整合，扫描指定包下的Mapper接口并绑定数据源
 *
 * @author PY.Lu
 * @date 2025/3/6
 */
@Slf4j
@Configuration
// 1. 扫描指定包下的Mapper接口，生成代理实现类
// 2. 指定使用名为"sqlSessionFactory"的SqlSessionFactoryBean实例
@MapperScan(basePackages = "com.logdemo.lightserver.Mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisConfiguration {
    /**
     * 配置并创建数据源(DataSource) Bean
     * 数据源是数据库连接的核心组件，负责管理数据库连接池、获取/释放连接等
     *
     * @return DataSource 配置好的数据源对象
     */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() {
        System.out.println("dataSource bean created");
        return DataSourceBuilder.create().build(); // 使用Spring的DataSourceBuilder构建数据源，自动读取配置文件中的数据源配置
    }

    /**
     * 配置并创建SqlSessionFactory Bean
     * SqlSessionFactory是MyBatis的核心工厂类，用于创建SqlSession（数据库会话），
     * 是MyBatis操作数据库的入口
     *
     * @return SqlSessionFactory MyBatis会话工厂对象
     * @throws Exception 配置加载失败时抛出异常（如Mapper文件路径错误、数据源配置错误等）
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory() throws Exception {
        // 创建SqlSessionFactoryBean（Spring整合MyBatis的核心类）
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();

        // 为SqlSessionFactory设置数据源（关联上面配置的dataSource Bean）
        sessionFactoryBean.setDataSource(getDataSource());

        // 指定MyBatis的Mapper XML文件路径（扫描classpath下Mapper目录下的所有.xml文件）
        String mapperLocations = "classpath*:Mapper/*.xml";

        // 创建资源解析器，用于加载指定路径下的Mapper XML文件
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        // 设置Mapper XML文件的资源路径，让MyBatis能够找到SQL映射文件
        sessionFactoryBean.setMapperLocations(resolver.getResources(mapperLocations));

        // 构建并返回SqlSessionFactory实例
        return sessionFactoryBean.getObject();
    }


    /**
     * 配置并创建SqlSessionTemplate Bean
     * SqlSessionTemplate是Spring提供的SqlSession实现，是线程安全的，
     * 替代原生MyBatis的SqlSession，用于实际执行数据库操作（CRUD）
     *
     * @param sqlSessionFactory 注入上面配置的SqlSessionFactory Bean（通过@Qualifier指定名称）
     * @return SqlSessionTemplate 线程安全的MyBatis会话模板
     */
    @Bean(name = "SqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        // 创建SqlSessionTemplate并关联SqlSessionFactory，供Mapper接口代理类使用
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
