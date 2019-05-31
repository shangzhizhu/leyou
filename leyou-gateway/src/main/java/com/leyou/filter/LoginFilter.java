package com.leyou.filter;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.config.FilterProperties;
import com.leyou.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

//@Component
//@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {

        // 获取zuul的上下文对象
        RequestContext currentContext = RequestContext.getCurrentContext();

        // 获取请求对象
        HttpServletRequest request = currentContext.getRequest();

        // 获取请求路径
        String requestUrl = request.getRequestURL().toString();

        // 遍历白名单 判断请求的url是否在白名单内
        for (String allowPath : this.filterProperties.getAllowPaths()) {
            if (requestUrl.contains(allowPath)){
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {

        // 获取zuul的上下文对象
        RequestContext currentContext = RequestContext.getCurrentContext();

        // 获取请求对象
        HttpServletRequest request = currentContext.getRequest();

        // 获取请求中的token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        // 验证token
        try {
            // 校验通过 放行
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            // 校验出现异常
            e.printStackTrace();
            // 不转发请求
            currentContext.setSendZuulResponse(false);
            // 返回校验失败验证码401
            currentContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        return null;
    }
}
