package ec.backend.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class UserDeptDTO implements Serializable {
    private static final long serialVersionUID = 1L; // 建议加上

    private Long id;
    private String username;
    private String realName;
    private String phone;
    private Integer status;
    private String deptName; // ★ 联表查出来的部门名称
}