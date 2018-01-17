package com.moekr.blog.util;

import com.moekr.blog.web.flexmark.CustomHtmlWriter;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ToolKit {
    public static Map<String, Object> emptyResponseBody() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", 0);
        return responseBody;
    }

    public static Map<String, Object> assemblyResponseBody(Object res) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", 0);
        responseBody.put("res", res);
        return responseBody;
    }

    public static Map<String, Object> assemblyResponseBody(int error, String message) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", error);
        responseBody.put("message", message);
        return responseBody;
    }

    public static <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    public static <T, K, V> Map<K, V> iterableToMap(Iterable<T> iterable, Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        Map<K, V> map = new HashMap<>();
        iterable.forEach(t -> map.put(keyGenerator.apply(t), valueGenerator.apply(t)));
        return map;
    }

    public static <T> List<T> sort(List<T> list, Comparator<T> comparator) {
        list.sort(comparator);
        return list;
    }

    public static <I extends Serializable, E> void assertNotNull(I id, E entity) {
        if (entity == null) {
            throw new ServiceException(ServiceException.NOT_FOUND, "Not Found");
        }
    }

    public static void assertPattern(CharSequence arg, Pattern pattern) {
        Matcher matcher = pattern.matcher(arg);
        if (!matcher.matches()) {
            throw new ServiceException(ServiceException.BAD_REQUEST, "Bad Request");
        }
    }

    public static void assertVisible(Visible visible) {
        if (!visible.isVisible()) {
            throw new ServiceException(ServiceException.NOT_FOUND, "Not Found");
        }
    }

    public static <T> T copyProperties(Object source, Supplier<T> targetSupplier) {
        T target = targetSupplier.get();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static String convertStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private static final Parser PARSER = Parser.builder().build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().build();

    public static String parseMarkdown(String markdown) {
        CustomHtmlWriter htmlWriter = new CustomHtmlWriter();
        RENDERER.render(PARSER.parse(markdown), htmlWriter);
        return htmlWriter.toString();
    }

    public static HttpStatus httpStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public static String remoteAddress(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        if (header == null) {
            return request.getRemoteAddr();
        }
        return header.split(",")[0];
    }
}
