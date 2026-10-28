package ec.backend.demo.controller;


import ec.backend.demo.common.Result;
import ec.backend.demo.dto.UserDeptDTO;
import ec.backend.demo.dto.UserCreateDto;
import ec.backend.demo.dto.UserUpdateDto;
import ec.backend.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/getUserWithDeptById")
    public Result<UserDeptDTO> getUserWithDeptById(Long userId){
        return userService.getUserWithDeptById(userId);
    }

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param updateDto 更新数据
     * @return 操作结果
     */
    @PostMapping("/updateUser")
    public Result<String> updateUser(@RequestBody UserUpdateDto updateDto, Long userId){
        return userService.updateUser(userId, updateDto);
    }

    @PostMapping("/create")
    public Result<String> createUser(@RequestBody UserCreateDto createDto){
        return userService.createUser(createDto);
    }
}
