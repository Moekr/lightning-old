package com.moekr.lightning.web.controller.api;

import com.moekr.lightning.logic.service.PropertyService;
import com.moekr.lightning.util.ToolKit;
import com.moekr.lightning.web.dto.PropertyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PropertyController {
    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/properties")
    public Map<String, Object> getProperties() {
        return ToolKit.assemblyResponseBody(propertyService.getProperties());
    }

    @GetMapping("/property/{propertyId}")
    public Map<String, Object> getProperty(@PathVariable String propertyId) {
        return ToolKit.assemblyResponseBody(propertyService.getProperty(propertyId));
    }

    @PutMapping("/property/{propertyId}")
    public Map<String, Object> updateProperty(@PathVariable String propertyId, @RequestBody @Valid PropertyDTO propertyDTO) {
        return ToolKit.assemblyResponseBody(propertyService.updateProperty(propertyId, propertyDTO));
    }
}
