package ec.backend.demo.controller;

import ec.backend.demo.entity.SysUser;
import ec.backend.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/users")
    public List<SysUser> getAllUsers() {
        // 查询所有用户，MyBatis-Plus 内置方法
        return userMapper.selectList(null);
    }
}