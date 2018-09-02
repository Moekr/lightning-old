package com.moekr.lightning.web.controller.redirect;

import com.moekr.lightning.logic.service.RedirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/r")
public class RedirectController {
    private final RedirectionService redirectionService;

    @Autowired
    public RedirectController(RedirectionService redirectionService) {
        this.redirectionService = redirectionService;
    }

    @GetMapping("/{redirectionId}")
    public void redirect(HttpServletResponse response, @PathVariable String redirectionId) {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader(HttpHeaders.LOCATION, redirectionService.viewRedirection(redirectionId).getLocation());
    }
}
