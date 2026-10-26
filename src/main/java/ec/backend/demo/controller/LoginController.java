package ec.backend.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ec.backend.demo.common.Result;
import ec.backend.demo.dto.LoginDto;
import ec.backend.demo.entity.SysUser;
import ec.backend.demo.mapper.UserMapper;
import ec.backend.demo.untils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

//注册登录接口
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final JwtUtil jwtUtil;


    private final UserMapper userMapper;

    // 注入 PasswordEncoder
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDto dto, TimeZone timeZone){
        Result<String> sysUserResult = new Result<>();
        if(dto.getUsername() == null || dto.getPassword() == null){
//            sysUserResult.setCode(500);
            sysUserResult.setMessage("用户名密码输入为空");
            return sysUserResult;
        }
        List<SysUser> sysUsers = userMapper.selectList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername()));
        if(sysUsers.size() > 0){
//            if(sysUsers.get(0).getPassword().equals(dto.getPassword())){
//                sysUserResult.setData(UUID.randomUUID().toString().replace("-", ""));
//                sysUserResult.setCode(200);
//                sysUserResult.setMessage("登录成功，返回token");
//                return sysUserResult;
//            }else{
//                sysUserResult.setCode(500);
//                sysUserResult.setMessage("密码错误");
//                return sysUserResult;
//            }

            // ★ 把这里的变量名换成你实际代码里的变量名
            System.out.println("===== 调试开始 =====");
            System.out.println("前端传来的密码是：[" + dto.getPassword() + "]");
            System.out.println("数据库里的密文是：[" + sysUsers.get(0).getPassword() + "]");

            // 1. 把明文和密文直接写死（用我刚才给你生成的那个密文）
            boolean testMatch = passwordEncoder.matches("123456", "$2a$10$rsmMYA5HiL7DpEJkOIJ0.ui/H2QzmRx4wNIre5U6JnOH4udbnZaZe");
            System.out.println("===== 硬编码对比结果：" + testMatch + " =====");

            // 2. 对比一下你查出来的密文，和写死的密文长度、是否相等
            String dbPwd = sysUsers.get(0).getPassword();
            System.out.println("===== 数据库密文长度：" + dbPwd.length() + " =====");
            System.out.println("===== 数据库密文是否等于硬编码：" + dbPwd.equals("$2a$10$rsmMYA5HiL7DpEJkOIJ0.ui/H2QzmRx4wNIre5U6JnOH4udbnZaZe") + " =====");

            System.out.println("当前使用的编码器类型是：" + passwordEncoder.getClass().getName());

            // 校验密码
            if (passwordEncoder.matches(dto.getPassword(), sysUsers.get(0).getPassword())) {
                sysUserResult.setData( jwtUtil.generateToken(sysUsers.get(0).getId(),sysUsers.get(0).getUsername()));
                sysUserResult.setCode(200);
                sysUserResult.setMessage("登录成功，返回token");
                return sysUserResult;
            }else{
                sysUserResult.setCode(500);
                sysUserResult.setMessage("密码错误");
                return sysUserResult;
            }
        }else{
            sysUserResult.setCode(500);
            sysUserResult.setMessage("用户不存在");
            return sysUserResult;
        }
    }
}
