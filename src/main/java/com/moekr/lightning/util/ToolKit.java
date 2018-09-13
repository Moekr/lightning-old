package com.moekr.lightning.util;

import com.moekr.lightning.web.flexmark.CustomHtmlWriter;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Supplier;

public abstract class ToolKit {
    public static final Charset CHARSET = Charset.forName("UTF-8");
    public static final String CHINESE_NAME = "萌客";
    public static final String ENGLISH_NAME = "Moekr";
    public static final String FULL_NAME = CHINESE_NAME + " - " + ENGLISH_NAME;

    public static LightningProperties properties;

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

    public static <T> List<T> sort(List<T> list, Comparator<T> comparator) {
        list.sort(comparator);
        return list;
    }

    public static String parseTags(Set<String> tags) {
        if (tags.isEmpty()) {
            return "无可用标签";
        }
        List<String> sortedTags = sort(new ArrayList<>(tags), String::compareTo);
        return String.join(" / ", sortedTags);
    }

    public static <T> T defaultIfNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static <I extends Serializable, E> void assertNotNull(I id, E entity) {
        if (entity == null) {
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

    private static final Iterable<Extension> EXTENSION = Collections.singletonList(AttributesExtension.create());
    private static final Parser PARSER = Parser.builder().extensions(EXTENSION).build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().build();

    public static String parseMarkdown(String markdown) {
        CustomHtmlWriter htmlWriter = new CustomHtmlWriter();
        RENDERER.render(PARSER.parse(markdown), htmlWriter);
        return htmlWriter.toString();
    }

    public static String compressScript(String raw) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(raw.getBytes(CHARSET));
        Reader reader = new InputStreamReader(inputStream, CHARSET);
        JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new ToolErrorReporter(true));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream, CHARSET);
        compressor.compress(writer, -1, true, true, false, false);
        writer.flush();
        return new String(outputStream.toByteArray(), CHARSET);
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
