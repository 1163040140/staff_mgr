package ec.backend.demo.dto;

import lombok.Data;

@Data
public class UserPageQuery {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String username;
    private Integer status;
}