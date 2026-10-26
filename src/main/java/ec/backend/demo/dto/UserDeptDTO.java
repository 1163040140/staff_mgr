package ec.backend.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDeptDTO {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private Integer status;
    private String deptName; // ★ 联表查出来的部门名称
}