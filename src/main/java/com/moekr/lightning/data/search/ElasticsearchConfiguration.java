package com.moekr.lightning.data.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.plugin.analysis.ik.AnalysisIkPlugin;
import org.elasticsearch.plugins.Plugin;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.moekr.lightning.data.search")
public class ElasticsearchConfiguration implements DisposableBean {
    private static final Log LOG = LogFactory.getLog(ElasticsearchConfiguration.class);
    private static final Collection<Class<? extends Plugin>> PLUGINS = Collections.singletonList(AnalysisIkPlugin.class);

    public static final String ANALYZER = "ik_max_word";

    private ElasticsearchProperties properties;
    private Node node;

    @Bean
    public ElasticsearchProperties elasticsearchProperties() {
        properties = new ElasticsearchProperties();
        return properties;
    }

    @Bean
    @DependsOn("elasticsearchProperties")
    public Client elasticsearchClient() throws NodeValidationException {
        Settings.Builder settings = Settings.builder();
        settings.put("transport.type", "local");
        settings.put("http.enabled", false);
        settings.put("cluster.name", properties.getClusterName());
        String home = properties.getProperties().getOrDefault("home", "./");
        settings.put("path.home", home);
        settings.put("path.data", home + "/data");
        node = new CustomNode(settings.build()).start();
        return node.client();
    }

    private static class CustomNode extends Node {
        private CustomNode(Settings settings) {
            super(InternalSettingsPreparer.prepareEnvironment(settings, null), PLUGINS);
        }
    }

    @Override
    public void destroy() {
        if (node != null) {
            try {
                LOG.info("Closing Elasticsearch client");
                node.close();
            } catch (Exception e) {
                LOG.error("Error closing Elasticsearch client: ", e);
            }
        }
    }
}
