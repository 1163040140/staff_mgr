package ec.backend.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ec.backend.demo.dto.UserDeptDTO;
import ec.backend.demo.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    @Select("SELECT u.id, u.username, u.real_name AS realName, u.phone, u.status, d.dept_name AS deptName " +
            "FROM sys_user u " +
            "LEFT JOIN sys_dept d ON u.dept_id = d.id " +
            "WHERE u.is_deleted = 0 AND u.id = #{id}")
    UserDeptDTO getUserWithDeptById(@Param("id") Long id);
}