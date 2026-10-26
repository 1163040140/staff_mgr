package ec.backend.demo.controller;


import ec.backend.demo.common.Result;
import ec.backend.demo.dto.UserDeptDTO;
import ec.backend.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/getUserWithDeptById")
    public Result<UserDeptDTO> getUserWithDeptById(Long userId){
        return new Result<>().ok(userService.getUserWithDeptById(userId));
    }
}
