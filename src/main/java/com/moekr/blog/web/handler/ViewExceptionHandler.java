package com.moekr.blog.web.handler;

import com.moekr.blog.logic.service.CategoryService;
import com.moekr.blog.logic.service.PropertyService;
import com.moekr.blog.util.ServiceException;
import com.moekr.blog.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice("com.moekr.blog.web.controller.view")
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
    public String handle(HttpServletRequest request, Exception exception)  {
        request.setAttribute("properties", propertyService.getPropertiesAsMap());
        request.setAttribute("categories", categoryService.getCategories());
        if(exception instanceof ServiceException){
            request.setAttribute("error", ((ServiceException)exception).getError());
        }else{
            request.setAttribute("error", DEFAULT_ERROR_CODE);
            request.setAttribute("stack", ToolKit.convertStackTrace(exception));
        }
        request.setAttribute("message", exception.getMessage());
        return "err";
    }
}
