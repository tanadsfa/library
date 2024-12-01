package com.example.demo1.filter;

import com.alibaba.fastjson.JSON;
import com.example.demo1.config.DemoConfiguration;
import com.example.demo1.constant.RedisKey;
import com.example.demo1.domain.dto.SecurityUser;
import com.example.demo1.utils.JwtUtil;
import com.example.demo1.utils.R;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private Logger log = LoggerFactory.getLogger(getClass());
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DemoConfiguration.Security security;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(JwtUtil.TOKEN_HEADER);
        log.info("intercept " + request.getRequestURI());
        
        if (isFilterRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = null;
        try {
            claims = JwtUtil.parseToken(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            fallback("token解析失败(非法token)！", response);
            return;
        }
        String username = claims.getSubject();
        String cache = (String) redisTemplate.opsForValue().get(String.format(RedisKey.AUTH_TOKEN_KEY, username));
        if (cache == null) {
            fallback("token失效，请重新登录！", response);
            return;
        }
        SecurityUser user = JSON.parseObject(cache, SecurityUser.class);
        log.info(JSON.toJSONString(user, true));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request, response);
    }

    private void fallback(String message, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = null;
        try {
            if (message == null) {
                message = "403 Forbidden";
            }
            R res = R.error(403, message);
            writer = response.getWriter();
            writer.append(JSON.toJSONString(res));
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private boolean isFilterRequest(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String filterPath = request.getRequestURI();
        List<String> permitAllPathList = security.getPermitAllPath();
        if (CollectionUtils.isEmpty(permitAllPathList)) {
            return false;
        }
        for (String path : permitAllPathList) {
            String pattern = contextPath + path;
            pattern = pattern.replaceAll("/+", "/");
            if (pathMatcher.match(pattern, filterPath)) {
                return true;
            }
        }
        return false;
    }
}
