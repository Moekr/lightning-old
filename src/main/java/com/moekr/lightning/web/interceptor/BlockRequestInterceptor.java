package com.moekr.lightning.web.interceptor;

import com.moekr.lightning.logic.service.AuthorityService;
import com.moekr.lightning.util.ToolKit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BlockRequestInterceptor extends HandlerInterceptorAdapter {
    private static final Log LOGGER = LogFactory.getLog(BlockRequestInterceptor.class);

    private final AuthorityService authorityService;

    @Autowired
    public BlockRequestInterceptor(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String remoteAddress = ToolKit.remoteAddress(request);
        if (authorityService.isBlocked(remoteAddress)) {
            LOGGER.info("Blocked request from: " + remoteAddress);
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
