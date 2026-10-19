package ec.backend.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ec.backend.demo.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
}