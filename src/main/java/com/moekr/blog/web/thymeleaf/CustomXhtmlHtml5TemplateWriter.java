package com.moekr.blog.web.thymeleaf;

import org.apache.commons.lang.StringUtils;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Text;
import org.thymeleaf.templatewriter.AbstractGeneralTemplateWriter;

import java.io.IOException;
import java.io.Writer;

public class CustomXhtmlHtml5TemplateWriter extends AbstractGeneralTemplateWriter {

    @Override
    protected void writeText(Arguments arguments, Writer writer, Text text) throws IOException {
        String content = text.getContent();
        if (StringUtils.isNotBlank(content)) {
            super.writeText(arguments, writer, text);
        }
    }

    @Override
    protected boolean shouldWriteXmlDeclaration() {
        return false;
    }

    @Override
    protected boolean useXhtmlTagMinimizationRules() {
        return true;
    }
}
