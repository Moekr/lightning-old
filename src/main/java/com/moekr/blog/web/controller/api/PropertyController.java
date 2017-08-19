package com.moekr.blog.web.controller.api;

import com.moekr.blog.logic.service.PropertyService;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.PropertyDto;
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
    public Map<String, Object> getProperties(){
        return ToolKit.assemblyResponseBody(propertyService.getProperties());
    }

    @GetMapping("/property/{propertyId}")
    public Map<String, Object> getProperty(@PathVariable String propertyId){
        return ToolKit.assemblyResponseBody(propertyService.getProperty(propertyId));
    }

    @PutMapping("/property/{propertyId}")
    public Map<String, Object> updateProperty(@PathVariable String propertyId, @RequestBody @Valid PropertyDto propertyDto){
        return ToolKit.assemblyResponseBody(propertyService.updateProperty(propertyId, propertyDto));
    }
}
