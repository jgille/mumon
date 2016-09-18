package org.jgille.mumon.application.report.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class ServiceInstanceHealth {

    private final String serviceName;
    private final String serviceVersion;
    private final String instanceId;
    private final String hostAddress;
    private final Instant timestamp;

    private final Status status;
    private final List<HealthCheck> healthChecks;

    private ServiceInstanceHealth(Builder builder) {
        this.serviceName = builder.serviceName;
        this.serviceVersion = builder.serviceVersion;
        this.instanceId = builder.instanceId;
        this.hostAddress = builder.hostAddress;
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.healthChecks = new ArrayList<>(builder.healthChecks);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String serviceName() {
        return serviceName;
    }

    public String serviceVersion() {
        return serviceVersion;
    }

    public String instanceId() {
        return instanceId;
    }

    public String hostAddress() {
        return hostAddress;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public Status status() {
        return status;
    }

    public List<HealthCheck> healthChecks() {
        return Collections.unmodifiableList(healthChecks);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    public static class Builder {
        private String serviceName;
        private String serviceVersion;
        private String instanceId;
        private String hostAddress;
        private Instant timestamp;
        private Status status;
        private List<HealthCheck> healthChecks;

        public Builder withServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder withServiceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        public Builder withInstanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public Builder withHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
            return this;
        }

        public Builder withTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder withHealthChecks(List<HealthCheck> healthChecks) {
            this.healthChecks = healthChecks;
            return this;
        }

        public ServiceInstanceHealth build() {
            return new ServiceInstanceHealth(this);
        }
    }

}
