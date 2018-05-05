package com.parkhomenko;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteConcernResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private AdminDao adminDao;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //For quik tests purpose
        adminDao.signUpByPassword(new AdminDto("hachiko", "hachiko"));
    }

    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return action -> {
            System.out.println("Using Write Concern of Acknowledged !!!");
            return WriteConcern.ACKNOWLEDGED;
        };
    }

//    @Bean
//    public ConfigureMongo configureMongo() {
//        return new ConfigureMongo();        
//    }
//    
//    private static class ConfigureMongo implements BeanPostProcessor {
//        
//         @Override
//        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//
//            if(bean instanceof MongoTemplate) {
//                ((MongoTemplate)bean).setWriteConcern(WriteConcern.ACKNOWLEDGED);
//            }
//
//            return bean;
//        }
//    }
}
