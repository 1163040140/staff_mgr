package ec.backend.demo.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserUpdateDto implements Serializable {
    private static final long serialVersionUID = 1L;

    // 用户姓名
    private String realName;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
    // 状态 (0正常 1停用)
    private Integer status;
}