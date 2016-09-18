package org.jgille.mumon.application.report.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class ServiceHealth {

    private final String serviceName;
    private final List<ServiceInstanceHealth> instances;

    public ServiceHealth(String serviceName, List<ServiceInstanceHealth> instances) {
        this.serviceName = serviceName;
        this.instances = new ArrayList<>(instances);
        this.instances.sort(this::compareInstances);
    }

    private int compareInstances(ServiceInstanceHealth i1, ServiceInstanceHealth i2) {
        // Sort based on status and time
        if (i1.status() == i2.status()) {
            return i2.timestamp().compareTo(i1.timestamp());
        } else {
            return i2.status().ordinal() - i1.status().ordinal();
        }
    }

    public String serviceName() {
        return serviceName;
    }

    public List<ServiceInstanceHealth> instances() {
        return Collections.unmodifiableList(instances);
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

}
