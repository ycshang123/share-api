package com.ycshang.userservice.auth;


import com.ycshang.userservice.service.RedisService;
import com.ycshang.userservice.utils.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthAspect {
    private final JwtOperator jwtOperator;
    private final RedisService redisService;


    @Around("@annotation(com.ycshang.userservice.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        checkToken();
        return point.proceed();
    }


    private void checkToken() {
        try {
//        1、从header中获取token
            HttpServletRequest request = getHttpServletRequest();
            String token = redisService.getValue("x-token",String.class);
//       2、校验token是否过期，如果不合法或者已过期直接抛异常，否则合法放行
            Boolean isValid = jwtOperator.validateToken(token);
            if (!isValid) {
                throw new SecurityException("Token已过期");
            }
//     3、如果校验成功，将永不信息设置到request的attribute里面
            Claims claims = jwtOperator.getClaimsFromToken(token);

            request.setAttribute("id", claims.get("id"));
            request.setAttribute("nickname", claims.get("nickname"));
            request.setAttribute("role", claims.get("role"));
        } catch (Throwable throwable) {
            throw new SecurityException("Token不合法");
        }
    }


    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        assert attributes != null;
        return attributes.getRequest();
    }


    @Around("@annotation(com.ycshang.userservice.auth.CheckAuthorization)")
    public Object checkAuthorization(ProceedingJoinPoint point) throws Throwable {
        try {
//            校验token是否合法
            this.checkToken();
//            验证用户角色是否匹配
            HttpServletRequest request = getHttpServletRequest();
            String role = (String) request.getAttribute("role");

            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();

            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);
            String value = annotation.value();

            if (
                    !Objects.equals(role, value)
            ) {
                throw new SecurityException("用户无权限访问!");
            }

        } catch (Throwable throwable) {
            throw new SecurityException("用户无权限访问", throwable);

        }

        return point.proceed();

    }
}
