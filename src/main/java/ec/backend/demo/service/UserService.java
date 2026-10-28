package ec.backend.demo.service;

import ec.backend.demo.common.Result;
import ec.backend.demo.dto.UserCreateDto;
import ec.backend.demo.dto.UserDeptDTO;
import ec.backend.demo.dto.UserUpdateDto;
import ec.backend.demo.entity.SysUser;
import ec.backend.demo.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserService extends ServiceImpl<UserMapper, SysUser> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Page<SysUser> getUserPage(int pageNum, int pageSize, String username, Integer status) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.eq(SysUser::getUsername, username);
        }
        if (status != null) {
            wrapper.eq(SysUser::getStatus, status);
        }
        Page<SysUser> result = this.page(page, wrapper);
        return result;
    }

    public Result<UserDeptDTO> getUserWithDeptById(Long id){
        String cacheKey = "user:dept:" + id;

        // 1. 先查 Redis
        Object cacheData = redisTemplate.opsForValue().get(cacheKey);
        if (cacheData != null) {
            System.out.println("===== 走了 Redis 缓存 =====");
            return Result.ok((UserDeptDTO) cacheData) ;
        }

        // 2. Redis 没有，查 MySQL（用你的多表联查 Mapper）
        System.out.println("===== 走了 MySQL 数据库 =====");
        UserDeptDTO userDept = userMapper.getUserWithDeptById(id);
        if (userDept == null) {
            return Result.fail(400,"用户不存在",null);
        }

        // 3. 把查出来的数据写回 Redis，设置 10 分钟过期（防止缓存堆积）
        redisTemplate.opsForValue().set(cacheKey, userDept, 10, TimeUnit.MINUTES);

        return Result.ok(userDept) ;
    }

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param updateDto 更新数据
     * @return 操作结果
     */
    public Result<String> updateUser(Long userId, UserUpdateDto updateDto) {
        // 1. 校验用户是否存在
        SysUser sysUser = userMapper.selectById(userId);
        if (sysUser == null) {
            return Result.fail(400, "用户不存在", null);
        }

        // 2. 更新用户信息（只更新非空字段）
        BeanUtils.copyProperties(updateDto, sysUser, "id", "username", "password", "createTime", "deptId", "isDeleted");

        // 3. 执行更新
        boolean updated = this.updateById(sysUser);

        if (updated) {
            // 更新成功后清除缓存
            String cacheKey = "user:dept:" + userId;
            redisTemplate.delete(cacheKey);
            return Result.ok("更新成功");
        } else {
            return Result.fail("更新失败");
        }
    }

    /**
     * 新增用户
     * @param createDto 用户创建数据
     * @return 操作结果
     */
    public Result<String> createUser(UserCreateDto createDto) {
        // 1. 检查用户名是否已存在
        SysUser existUser = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, createDto.getUsername()));
        if (existUser != null) {
            return Result.fail(400, "用户名已存在", null);
        }

        // 2. 创建 SysUser 对象
        SysUser sysUser = new SysUser();
        sysUser.setUsername(createDto.getUsername());
        sysUser.setPassword(passwordEncoder.encode(createDto.getPassword())); // 需要密码编码
        sysUser.setRealName(createDto.getRealName());
        sysUser.setPhone(createDto.getPhone());
        sysUser.setEmail(createDto.getEmail());
        sysUser.setStatus(createDto.getStatus() != null ? createDto.getStatus() : 0);
        sysUser.setIsDeleted(0);

        // 3. 插入数据库
        boolean saved = this.save(sysUser);

        if (saved) {
            return Result.ok("新增成功");
        } else {
            return Result.fail("新增失败");
        }
    }
}