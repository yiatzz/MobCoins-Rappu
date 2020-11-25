package br.com.yiatzz.mobcoins.cache.local;

import br.com.yiatzz.mobcoins.MobCoinsPlugin;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MobCoinsLocalCache {

    private final LoadingCache<String, Double> CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Double>() {
                @Override
                public Double load(String key) {
                    return MobCoinsPlugin.getRepository().fetchMobCoins(key);
                }
            });

    public Double get(String userName) {
        try {
            return CACHE.get(userName);
        } catch (ExecutionException e) {
            return null;
        }
    }

    public void invalidate(String userName) {
        CACHE.invalidate(userName);
    }
}
