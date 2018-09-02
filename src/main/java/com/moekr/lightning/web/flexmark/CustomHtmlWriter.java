package com.moekr.lightning.web.flexmark;

import org.apache.commons.lang.StringUtils;

public class CustomHtmlWriter implements Appendable {
    private final StringBuilder stringBuilder;

    public CustomHtmlWriter() {
        stringBuilder = new StringBuilder();
    }

    @Override
    public Appendable append(CharSequence csq) {
        if (!StringUtils.equals("\n", csq.toString())) {
            stringBuilder.append(csq);
        }
        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) {
        if (!StringUtils.equals("\n", csq.toString())) {
            stringBuilder.append(csq, start, end);
        }
        return this;
    }

    @Override
    public Appendable append(char c) {
        if ('\n' != c) {
            stringBuilder.append(c);
        }
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
