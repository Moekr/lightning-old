package com.moekr.lightning.web.thymeleaf;

import com.moekr.lightning.util.ToolKit;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IPostProcessorDialect;
import org.thymeleaf.engine.AbstractTemplateHandler;
import org.thymeleaf.engine.ITemplateHandler;
import org.thymeleaf.model.ICloseElementTag;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.IText;
import org.thymeleaf.postprocessor.IPostProcessor;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@CommonsLog
@Configuration
public class ThymeleafConfiguration {
    private final ThymeleafProperties properties;
    private final Collection<ITemplateResolver> templateResolvers;
    private final Collection<IDialect> dialects;

    @Autowired
    public ThymeleafConfiguration(ThymeleafProperties properties, Collection<ITemplateResolver> templateResolvers,
                                  ObjectProvider<Collection<IDialect>> dialectsProvider) {
        this.properties = properties;
        this.templateResolvers = templateResolvers;
        this.dialects = dialectsProvider.getIfAvailable(Collections::emptyList);
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(this.properties.isEnableSpringElCompiler());
        this.templateResolvers.forEach(engine::addTemplateResolver);
        this.dialects.forEach(engine::addDialect);
        engine.addDialect(new RemoveBlankTextDialect());
        return engine;
    }

    private static class RemoveBlankTextDialect implements IPostProcessorDialect {
        @Override
        public int getDialectPostProcessorPrecedence() {
            return SpringStandardDialect.PROCESSOR_PRECEDENCE;
        }

        @Override
        public Set<IPostProcessor> getPostProcessors() {
            return Collections.singleton(new BlankTextProcessor());
        }

        @Override
        public String getName() {
            return "RemoveBlankTextDialect";
        }
    }

    private static class BlankTextProcessor implements IPostProcessor {
        @Override
        public TemplateMode getTemplateMode() {
            return TemplateMode.HTML;
        }

        @Override
        public int getPrecedence() {
            return SpringStandardDialect.PROCESSOR_PRECEDENCE;
        }

        @Override
        public Class<? extends ITemplateHandler> getHandlerClass() {
            return BlankTextHandler.class;
        }
    }

    public static class BlankTextHandler extends AbstractTemplateHandler {
        private boolean inScript = false;

        @Override
        public void handleText(IText text) {
            if (!StringUtils.isBlank(text)) {
                if (inScript) {
                    try {
                        text = super.getContext().getModelFactory().createText(ToolKit.compressScript(text.getText()));
                    } catch (IOException e) {
                        log.error("Compress script error: " + e.getMessage());
                    }
                }
                super.handleText(text);
            }
        }

        @Override
        public void handleOpenElement(IOpenElementTag openElementTag) {
            if (openElementTag.getElementCompleteName().equals("script")) {
                inScript = true;
            }
            super.handleOpenElement(openElementTag);
        }

        @Override
        public void handleCloseElement(ICloseElementTag closeElementTag) {
            if (closeElementTag.getElementCompleteName().equals("script")) {
                inScript = false;
            }
            super.handleCloseElement(closeElementTag);
        }
    }
}
