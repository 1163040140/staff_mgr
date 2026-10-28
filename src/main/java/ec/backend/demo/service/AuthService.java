package ec.backend.demo.service;

import ec.backend.demo.common.Result;
import ec.backend.demo.dto.LoginDto;
import ec.backend.demo.entity.SysUser;
import ec.backend.demo.mapper.UserMapper;
import ec.backend.demo.untils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录业务逻辑
     *
     * @param dto 登录参数
     * @return 登录结果（包含 token）
     */
    public Result<String> login(LoginDto dto) {
        // 1. 参数校验
        if (dto.getUsername() == null || dto.getPassword() == null) {
            return Result.fail(500, "用户名密码输入为空");
        }

        // 2. 查询用户
        List<SysUser> sysUsers = userMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername())
        );

        if (sysUsers == null || sysUsers.isEmpty()) {
            return Result.fail(500, "用户不存在");
        }

        SysUser user = sysUsers.get(0);

        // 3. 校验密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return Result.fail(500, "密码错误");
        }

        // 4. 生成 token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return Result.ok(token);
    }
}