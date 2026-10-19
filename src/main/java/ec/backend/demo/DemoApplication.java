package ec.backend.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 */
@SpringBootApplication
// ★ 关键：告诉 MyBatis 去哪个包下扫描 Mapper 接口
@MapperScan("ec.backend.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("=========================================");
        System.out.println("  staff-mgr 系统启动成功！");
        System.out.println("  接口文档: http://localhost:8080/swagger-ui/index.html");
        System.out.println("=========================================");
    }
}