package ec.backend.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ec.backend.demo.common.Result;
import ec.backend.demo.dto.UserPageQuery;
import ec.backend.demo.entity.SysUser;
import ec.backend.demo.mapper.UserMapper;
import ec.backend.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<SysUser> getAllUsers() {
        // 查询所有用户，MyBatis-Plus 内置方法
        return userMapper.selectList(null);
    }

    @GetMapping("/page")
    public Result<Page<SysUser>> getUserPage(@ModelAttribute UserPageQuery query) {
        // 分页 + 动态条件查询（username / status 传了才参与过滤）
        Page<SysUser> page = userService.getUserPage(
                query.getPageNum(),
                query.getPageSize(),
                query.getUsername(),
                query.getStatus());
        return Result.ok(page);
    }
}