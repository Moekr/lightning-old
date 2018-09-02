package com.moekr.lightning.data.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.Version;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.plugin.analysis.ik.AnalysisIkPlugin;
import org.elasticsearch.plugins.Plugin;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class ElasticsearchConfiguration implements DisposableBean {
    private static final Log LOG = LogFactory.getLog(ElasticsearchConfiguration.class);
    private static final Collection<Class<? extends Plugin>> PLUGINS = Collections.singletonList(AnalysisIkPlugin.class);

    public static final String ANALYZER = "ik_max_word";

    private final ElasticsearchProperties properties;

    private Node node;

    @Autowired
    public ElasticsearchConfiguration(ElasticsearchProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Client elasticsearchClient() {
        Settings.Builder settings = Settings.settingsBuilder();
        settings.put("node.local", true);
        settings.put("http.enabled", false);
        settings.put("cluster.name", properties.getClusterName());
        settings.put("path.home", properties.getProperties().getOrDefault("home", ""));
        node = new CustomNode(settings.build()).start();
        return node.client();
    }

    private static class CustomNode extends Node {
        private CustomNode(Settings settings) {
            super(InternalSettingsPreparer.prepareEnvironment(settings, null), Version.CURRENT, PLUGINS);
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
