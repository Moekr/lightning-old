package com.moekr.lightning.web.controller.api;

import com.moekr.lightning.logic.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/amp")
public class AmpApiController {
    private final PropertyService propertyService;

    private Map<String, Object> triggers;

    @Autowired
    public AmpApiController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostConstruct
    private void initial() {
        Map<String, Object> trigger = new HashMap<>();
        trigger.put("on", "visible");
        trigger.put("request", "pageview");
        triggers = new HashMap<>();
        triggers.put("trigger", trigger);
    }

    @GetMapping("/analytics-config.json")
    public Map<String, Object> analyticsConfig(HttpServletRequest request, HttpServletResponse response) {
        if (triggers == null) {
            initial();
        }
        String origin = request.getParameter("__amp_source_origin");
        if (origin != null) {
            response.addHeader("AMP-Access-Control-Allow-Source-Origin", origin);
        }
        Map<String, Object> vars = new HashMap<>();
        vars.put("account", propertyService.getProperty("analytics").getValue());
        Map<String, Object> res = new HashMap<>();
        res.put("vars", vars);
        res.put("triggers", triggers);
        return res;
    }
}
