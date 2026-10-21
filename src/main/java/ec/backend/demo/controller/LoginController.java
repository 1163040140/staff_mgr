package ec.backend.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ec.backend.demo.common.Result;
import ec.backend.demo.dto.LoginDto;
import ec.backend.demo.entity.SysUser;
import ec.backend.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

//注册登录接口
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserMapper userMapper;

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
            if(sysUsers.get(0).getPassword().equals(dto.getPassword())){
                sysUserResult.setData(UUID.randomUUID().toString().replace("-", ""));
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
