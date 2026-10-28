package ec.backend.demo.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserCreateDto implements Serializable {
    private static final long serialVersionUID = 1L;

    // 用户名
    private String username;
    // 密码
    private String password;
    // 真实姓名
    private String realName;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
    // 状态 (0正常 1停用)
    private Integer status;
    // 部门ID
    private Long deptId;
}