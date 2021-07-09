package com.example.jpa;

import com.example.jpa.config.H2JpaConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Launcher.class, H2JpaConfig.class})
@ActiveProfiles("it")
public class SpringBootInitialLoadIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void contextLoads() {
        Map resultMap = jdbcTemplate.queryForMap("select count(*) from MODEL");
        System.out.println(resultMap);
    }


}