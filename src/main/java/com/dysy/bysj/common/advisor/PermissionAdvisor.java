package com.dysy.bysj.common.advisor;

import com.dysy.bysj.common.Constants;
import com.dysy.bysj.common.annotation.RequirePermissions;
import com.dysy.bysj.common.enums.Logical;
import com.dysy.bysj.common.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Dai Junfeng
 * @create: 2021-01-27
 **/
@Aspect
@Component
public class PermissionAdvisor {

    @Pointcut("@annotation(com.dysy.bysj.common.annotation.RequirePermissions)")
    private void pointcut(){}

    @Before("pointcut() && @annotation(permissions)")
    // ProceedingJoinPoint is only supported for around advice，改为 JoinPoint
    public void before(JoinPoint joinPoint, RequirePermissions permissions) throws Throwable{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.ACCESS_TOKEN);
        // 通过 token 取出用户在缓存中的角色信息
        List<String> permissionsList = Arrays.asList("user:delete");
        // 比较是否有权限
        Logical logical = permissions.logical();
        List<String> requirePermissions = Arrays.asList(permissions.value());
        if (logical == Logical.AND) {
            if (!permissionsList.containsAll(requirePermissions)) {
                throw new BusinessException("无权限访问");
            }
        } else {
            permissionsList.retainAll(requirePermissions);
            if (permissionsList.size() <= 0) {
                throw new BusinessException("无权限访问");
            }
        }
    }
}
