package org.jgille.mumon.application.services

import org.jgille.mumon.application.report.domain.ServiceHealth
import org.jgille.mumon.application.report.domain.ServiceHealthRepository

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Produces(MediaType.APPLICATION_JSON)
@Path("services")
class ServiceResource {

    private final ServiceHealthRepository serviceHealthRepository

    ServiceResource(ServiceHealthRepository serviceHealthRepository) {
        this.serviceHealthRepository = serviceHealthRepository
    }

    @GET
    def getServiceStatuses() {
        // TODO: Compute service health
        serviceHealthRepository.currentSystemHealth().collect {
            serviceHealth -> [
                    name: serviceHealth.serviceName(),
                    status: 'HEALTHY'
            ]
        }
    }

    @GET
    @Path('{service_name}')
    def getServiceStatus(@PathParam('service_name') String serviceName) {
        def currentServiceHealth = serviceHealthRepository.currentServiceHealth(serviceName)
        [
                name: serviceName,
                status: computeServiceStatus(currentServiceHealth),
                instances: [
                        currentServiceHealth.instances().collect { instance ->
                            [
                                    id: instance.instanceId(),
                                    status: instance.status().name(),
                                    service_version: instance.serviceVersion(),
                                    host_address: instance.hostAddress(),
                                    health_checks: instance.healthChecks().collect { check ->
                                        [
                                                name: check.name(),
                                                status: check.status().name(),
                                                message: check.message()
                                        ]
                                    }
                            ]
                        }
                ]
        ]
    }

    def computeServiceStatus(ServiceHealth serviceHealth) {
        if (serviceHealth.instances()) {
            serviceHealth.instances()[0].status().name()
        } else {
            'DOWN'
        }
    }
}