package com.moekr.lightning.web.controller.error;

import com.moekr.lightning.logic.service.CategoryService;
import com.moekr.lightning.logic.service.PropertyService;
import com.moekr.lightning.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(GlobalErrorController.ERROR_PATH)
@ResponseStatus(HttpStatus.OK)
public class ViewErrorController {
    private final CategoryService categoryService;
    private final PropertyService propertyService;

    @Autowired
    public ViewErrorController(CategoryService categoryService, PropertyService propertyService) {
        this.categoryService = categoryService;
        this.propertyService = propertyService;
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String errorHtml(Map<String, Object> parameterMap, HttpServletRequest request) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", categoryService.getCategories());
        HttpStatus status = ToolKit.httpStatus(request);
        parameterMap.put("error", status.value());
        parameterMap.put("message", status.getReasonPhrase());
        return "err";
    }
}
