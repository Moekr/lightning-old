package com.moekr.lightning.web.security;

import com.moekr.lightning.logic.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private final AuthorityService authorityService;

    @Autowired
    public AuthenticationSuccessEventListener(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        authorityService.authorityAttemptSuccess(details.getRemoteAddress());
    }
}
