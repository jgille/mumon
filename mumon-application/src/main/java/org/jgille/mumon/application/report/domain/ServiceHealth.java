package org.jgille.mumon.application.report.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceHealth {

    private final String serviceName;
    private final List<ServiceInstanceHealth> instances;

    public ServiceHealth(String serviceName, List<ServiceInstanceHealth> instances) {
        this.serviceName = serviceName;
        this.instances = new ArrayList<>(instances);
        this.instances.sort(this::compareInstances);
    }

    private int compareInstances(ServiceInstanceHealth i1, ServiceInstanceHealth i2) {
        // Sort based on health and time
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

}
