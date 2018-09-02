package com.moekr.lightning.logic.ehcache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

public class EhCacheEventListener implements CacheEventListener<Object, Object> {
    private static final Log LOGGER = LogFactory.getLog(EhCacheEventListener.class);

    @Override
    public void onEvent(CacheEvent<?, ?> event) {
        LOGGER.info("Cache Event: " + event.getType() + " Key: " + event.getKey());
    }
}
