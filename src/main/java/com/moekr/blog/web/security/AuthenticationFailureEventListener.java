package com.moekr.blog.web.security;

import com.moekr.blog.logic.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final AuthorityService authorityService;

    @Autowired
    public AuthenticationFailureEventListener(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        authorityService.authorityAttemptFail(details.getRemoteAddress());
    }
}
