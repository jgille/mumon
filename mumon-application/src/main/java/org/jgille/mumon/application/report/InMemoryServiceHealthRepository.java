package org.jgille.mumon.application.report;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jgille.mumon.application.report.domain.ServiceHealth;
import org.jgille.mumon.application.report.domain.ServiceHealthRepository;
import org.jgille.mumon.application.report.domain.ServiceInstanceHealth;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class InMemoryServiceHealthRepository implements ServiceHealthRepository {

    private final Duration instanceCoolDownPeriod;

    private final Cache<String, InstanceCache> serviceCache;

    public InMemoryServiceHealthRepository(Duration serviceCoolDownPeriod, Duration instanceCoolDownPeriod) {
        this.instanceCoolDownPeriod = instanceCoolDownPeriod;

        this.serviceCache = CacheBuilder.newBuilder()
                .expireAfterWrite(serviceCoolDownPeriod.getSeconds(), TimeUnit.SECONDS)
                // TODO: Make this configurable
                .maximumSize(1_000)
                .build();
    }

    @Override
    public void register(ServiceInstanceHealth instanceHealth) {
        try {
            InstanceCache instanceCache = serviceCache.get(instanceHealth.serviceName(),
                    () -> new InstanceCache(instanceHealth.serviceName(), instanceCoolDownPeriod));
            instanceCache.register(instanceHealth);
            // Register again to make sure we don't expire services that are alive
            serviceCache.put(instanceHealth.serviceName(), instanceCache);
        } catch (ExecutionException e) {
            // TODO: Use dedicated exception
            throw new RuntimeException("Failed to register service health", e);
        }
    }

    @Override
    public List<ServiceHealth> currentSystemHealth() {
        Set<String> serviceNames = serviceCache.asMap().keySet();
        return serviceNames.stream().map(this::currentServiceHealth)
                .sorted((s1, s2) -> s1.serviceName().compareTo(s2.serviceName())).collect(Collectors.toList());
    }

    @Override
    public ServiceHealth currentServiceHealth(String serviceName) {
        Optional<InstanceCache> instanceCache = Optional.ofNullable(serviceCache.getIfPresent(serviceName));
        return instanceCache.map(this::fromInstanceCache)
                .orElse(new ServiceHealth(serviceName, Collections.emptyList()));
    }

    private ServiceHealth fromInstanceCache(InstanceCache instanceCache) {
        return new ServiceHealth(instanceCache.serviceName(), instanceCache.instances());
    }

    private static class InstanceCache {

        private final String serviceName;
        private final Cache<String, ServiceInstanceHealth> instanceCache;

        private InstanceCache(String serviceName, Duration instanceCoolDownPeriod) {
            this.serviceName = serviceName;
            this.instanceCache = CacheBuilder.newBuilder()
                    .expireAfterWrite(instanceCoolDownPeriod.getSeconds(), TimeUnit.SECONDS)
                    // TODO: Make this configurable
                    .maximumSize(100)
                    .build();
        }

        private String serviceName() {
            return serviceName;
        }

        private void register(ServiceInstanceHealth instanceHealth) {
            instanceCache.put(instanceHealth.instanceId(), instanceHealth);
        }

        private List<ServiceInstanceHealth> instances() {
            Collection<ServiceInstanceHealth> values = instanceCache.asMap().values();
            List<ServiceInstanceHealth> result = new ArrayList<>(values);
            result.sort((i1, i2) -> i1.instanceId().compareTo(i2.instanceId()));
            return result;
        }
    }
}
