package com.moekr.blog.web.controller.api;

import com.moekr.blog.logic.service.RedirectionService;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.RedirectionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RedirectionController {
    private final RedirectionService redirectionService;

    @Autowired
    public RedirectionController(RedirectionService redirectionService){
        this.redirectionService = redirectionService;
    }

    @PutMapping("/redirections")
    public Map<String, Object> createOrUpdateRedirection(@RequestBody @Valid RedirectionDto redirectionDto){
        return ToolKit.assemblyResponseBody(redirectionService.createOrUpdateRedirection(redirectionDto));
    }

    @GetMapping("/redirections")
    public Map<String, Object> getRedirections(){
        return ToolKit.assemblyResponseBody(redirectionService.getRedirections());
    }

    @GetMapping("/redirection/{redirectionId}")
    public Map<String, Object> getRedirection(@PathVariable String redirectionId){
        return ToolKit.assemblyResponseBody(redirectionService.getRedirection(redirectionId));
    }

    @DeleteMapping("/redirection/{redirectionId}")
    public Map<String, Object> deleteRedirection(@PathVariable String redirectionId){
        redirectionService.deleteRedirection(redirectionId);
        return ToolKit.emptyResponseBody();
    }
}
