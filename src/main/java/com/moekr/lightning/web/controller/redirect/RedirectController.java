package com.moekr.lightning.web.controller.redirect;

import com.moekr.lightning.logic.service.RedirectionService;
import com.moekr.lightning.logic.vo.RedirectionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/url")
public class RedirectController {
    private final RedirectionService redirectionService;

    @Autowired
    public RedirectController(RedirectionService redirectionService) {
        this.redirectionService = redirectionService;
    }

    @GetMapping("/{redirectionId}")
    public String redirect(@PathVariable String redirectionId) {
        RedirectionVO redirection = redirectionService.viewRedirection(redirectionId);
        return "redirect:" + redirection.getLocation();
    }
}
