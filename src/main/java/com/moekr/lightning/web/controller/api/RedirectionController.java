package com.moekr.lightning.web.controller.api;

import com.moekr.lightning.logic.service.RedirectionService;
import com.moekr.lightning.util.ToolKit;
import com.moekr.lightning.web.dto.RedirectionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RedirectionController {
    private final RedirectionService redirectionService;

    @Autowired
    public RedirectionController(RedirectionService redirectionService) {
        this.redirectionService = redirectionService;
    }

    @PutMapping("/redirections")
    public Map<String, Object> createOrUpdateRedirection(@RequestBody @Valid RedirectionDTO redirectionDTO) {
        return ToolKit.assemblyResponseBody(redirectionService.createOrUpdateRedirection(redirectionDTO));
    }

    @GetMapping("/redirections")
    public Map<String, Object> getRedirections() {
        return ToolKit.assemblyResponseBody(redirectionService.getRedirections());
    }

    @GetMapping("/redirection/{redirectionId}")
    public Map<String, Object> getRedirection(@PathVariable String redirectionId) {
        return ToolKit.assemblyResponseBody(redirectionService.getRedirection(redirectionId));
    }

    @DeleteMapping("/redirection/{redirectionId}")
    public Map<String, Object> deleteRedirection(@PathVariable String redirectionId) {
        redirectionService.deleteRedirection(redirectionId);
        return ToolKit.emptyResponseBody();
    }
}
