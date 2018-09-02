package com.moekr.lightning.logic.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorityService {
    private static final int MAX_ATTEMPT_FAIL_COUNT = 5;

    private LoadingCache<String, Integer> authorityAttemptCache;

    public AuthorityService() {
        authorityAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(@Nullable String key) {
                return 0;
            }
        });
    }

    private int getAttemptFailCount(String remoteAddress) {
        int attemptCount;
        try {
            attemptCount = authorityAttemptCache.get(remoteAddress);
        } catch (ExecutionException e) {
            attemptCount = 0;
        }
        return attemptCount;
    }

    public boolean isBlocked(String remoteAddress) {
        return getAttemptFailCount(remoteAddress) >= MAX_ATTEMPT_FAIL_COUNT;
    }

    public void authorityAttemptSuccess(String remoteAddress) {
        authorityAttemptCache.put(remoteAddress, 0);
    }

    public void authorityAttemptFail(String remoteAddress) {
        authorityAttemptCache.put(remoteAddress, getAttemptFailCount(remoteAddress) + 1);
    }
}
