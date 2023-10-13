package threestar.selectstar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication//(exclude = DataSourceAutoConfiguration.class)
//@ComponentScan(basePackages = {"threestar.selectstar"})
@MapperScan(value = {"threestar.selectstar.dao"})
public class SelectStarApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelectStarApplication.class, args);
    }

}
