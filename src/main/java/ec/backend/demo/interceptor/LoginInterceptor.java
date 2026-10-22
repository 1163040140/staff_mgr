package ec.backend.demo.interceptor;

import ec.backend.demo.untils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//登录拦截器
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头（Header）中获取 token
        String token = request.getHeader("token");

        // 2. 如果 token 为空，说明没登录，直接拦截
        if (token == null || token.trim().isEmpty()) {
            // 设置响应格式为 JSON，防止中文乱码
            response.setContentType("application/json;charset=UTF-8");
            // 手动返回一个 401 的 JSON 字符串（这里为了简单手写，真实项目会转成 Result 对象）
            response.getWriter().write("{\"code\":401,\"message\":\"未登录，请先获取token\",\"data\":null}");

            // 返回 false 代表不放行，请求到此结束
            return false;
        }

        // 3. 如果有 token，放行（练习阶段我们暂时不深入校验 token 的真伪）
        jwtUtil.parseToken(token);
        return true;
    }
}
