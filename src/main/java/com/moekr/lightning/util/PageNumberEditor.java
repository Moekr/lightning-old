package com.moekr.lightning.util;

import java.beans.PropertyEditorSupport;

public class PageNumberEditor extends PropertyEditorSupport {
    private static final int DEFAULT_PAGE_NUMBER = 1;

    @Override
    public void setValue(Object value) {
        if (value instanceof Integer) {
            super.setValue((Integer) value - 1);
        } else {
            super.setValue(value);
        }
    }

    @Override
    public void setAsText(String s) {
        try {
            setValue(Integer.valueOf(s));
        } catch (NumberFormatException e) {
            setValue(DEFAULT_PAGE_NUMBER);
        }
    }

    @Override
    public String getJavaInitializationString() {
        Object var1 = this.getValue();
        return var1 != null ? var1.toString() : "null";
    }
}
