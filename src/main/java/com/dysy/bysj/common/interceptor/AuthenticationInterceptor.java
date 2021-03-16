package com.dysy.bysj.common.interceptor;

import com.dysy.bysj.common.AppContext;
import com.dysy.bysj.common.Constants;
import com.dysy.bysj.common.Result;
import com.dysy.bysj.common.annotation.RequireAuthentication;
import com.dysy.bysj.common.enums.ResponseCodeEnum;
import com.dysy.bysj.common.exception.BusinessException;
import com.dysy.bysj.util.TokenUtils;
import com.dysy.bysj.vo.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Dai Junfeng
 * @create: 2021-01-27
 **/
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        RequireAuthentication requireAuthentication = ((HandlerMethod) handler).getMethodAnnotation(RequireAuthentication.class);

        // 如果不需要验证 token, 直接放行访问 controller
        if (Objects.isNull(requireAuthentication) || requireAuthentication.value() == false) {
            return true;
        }

        String token = request.getHeader(Constants.ACCESS_TOKEN);
        System.out.println("注解拦截器token= " + token);

        if (StringUtils.isEmpty(token)) {
            System.out.println("token 为空");
            responseJSON(response, ResponseCodeEnum.UNAUTHORIZED.getCode(),
                    ResponseCodeEnum.UNAUTHORIZED.getMessage());
        } else {
            try {
                boolean isValidated = tokenUtils.isValidatedToken(token);
                if (isValidated) {
                    UserVO currentUser = AppContext.getCurrentUser();
                    if (Objects.isNull(currentUser)) {
                        UserVO userVO = tokenUtils.getUserVO(Constants.REFRESH_TOKEN_CACHE_PREFIX + token);
                        AppContext.setCurrentUser(userVO);
                    }
                    return true;
                }
            } catch (Exception e) {
                UserVO userVO = tokenUtils.getUserVO(Constants.REFRESH_TOKEN_CACHE_PREFIX + token);
                if (userVO == null) {
                    throw e;
                }
                token = tokenUtils.refreshToken(token, userVO);
                System.out.println("刷新 token -> " + token);
                HashMap<String, String> map = new HashMap<>();
                map.put(Constants.ACCESS_TOKEN, token);
                // 更新 token 后, 修改头部 token 信息，便于后面获取最新的token
                modifyHeaders(map,  request);
                // 额外暴露请求头中的 token 字段, 否则前端无法获取到(大小写不明感)
                response.addHeader("Access-Control-Expose-Headers", Constants.HEADER_TOKEN);
                response.addHeader(Constants.HEADER_TOKEN, token);

                UserVO currentUser = AppContext.getCurrentUser();
                if (Objects.isNull(currentUser)) {
                    AppContext.setCurrentUser(userVO);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        AppContext.removeCurrentUser();
    }


    /**
     * 返回错误信息
     * @param response
     * @param code 状态码
     * @param msg 错误信息
     * @throws JsonProcessingException
     */
    private void responseJSON(HttpServletResponse response, int code, String msg) throws JsonProcessingException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        Result result = Result.error(code, msg);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);
        PrintWriter writer = null;

        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    /**
     * 修改请求头信息
     * @param headersMap 新的参数
     * @param request
     */
    private void modifyHeaders(Map<String, String> headersMap, HttpServletRequest request) {
        if (headersMap == null || headersMap.isEmpty()) {
            return;
        }
        Class<? extends HttpServletRequest> clazz = request.getClass();
        try {
            Field req = clazz.getDeclaredField("request");
            req.setAccessible(true);
            Object o = req.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object o1 = coyoteRequest.get(o);
            Field headers = o1.getClass().getDeclaredField("headers");
            headers.setAccessible(true);
            MimeHeaders o2 = (MimeHeaders)headers.get(o1);
            for (Map.Entry<String, String> entry : headersMap.entrySet()) {
                o2.removeHeader(entry.getKey());
                o2.addValue(entry.getKey()).setString(entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
