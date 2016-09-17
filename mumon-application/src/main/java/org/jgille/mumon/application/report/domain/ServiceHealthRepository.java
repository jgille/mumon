package org.jgille.mumon.application.report.domain;

import java.util.List;

public interface ServiceHealthRepository {

    void register(ServiceInstanceHealth instanceHealth);

    List<ServiceHealth> currentSystemHealth();

    ServiceHealth currentServiceHealth(String serviceName);
}
