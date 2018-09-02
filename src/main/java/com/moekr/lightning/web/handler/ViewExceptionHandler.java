package com.moekr.lightning.web.handler;

import com.moekr.lightning.logic.service.CategoryService;
import com.moekr.lightning.logic.service.PropertyService;
import com.moekr.lightning.util.ServiceException;
import com.moekr.lightning.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice({"com.moekr.lightning.web.controller.view", "com.moekr.lightning.web.controller.redirect"})
public class ViewExceptionHandler {
    private static final int DEFAULT_ERROR_CODE = 500;

    private final CategoryService categoryService;
    private final PropertyService propertyService;

    @Autowired
    public ViewExceptionHandler(CategoryService categoryService, PropertyService propertyService) {
        this.categoryService = categoryService;
        this.propertyService = propertyService;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public String handle(HttpServletRequest request, Exception exception) {
        request.setAttribute("properties", propertyService.getPropertiesAsMap());
        request.setAttribute("categories", categoryService.getCategories());
        if (exception instanceof ServiceException) {
            request.setAttribute("error", ((ServiceException) exception).getError());
        } else {
            request.setAttribute("error", DEFAULT_ERROR_CODE);
            request.setAttribute("stack", ToolKit.convertStackTrace(exception));
        }
        request.setAttribute("message", exception.getMessage());
        return "err";
    }
}
