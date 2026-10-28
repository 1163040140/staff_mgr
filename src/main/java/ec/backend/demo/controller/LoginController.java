package ec.backend.demo.controller;

import ec.backend.demo.common.Result;
import ec.backend.demo.dto.LoginDto;
import ec.backend.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//注册登录接口
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDto dto) {
        return authService.login(dto);
    }
}