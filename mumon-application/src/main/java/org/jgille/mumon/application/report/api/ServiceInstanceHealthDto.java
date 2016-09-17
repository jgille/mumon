package org.jgille.mumon.application.report.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ServiceInstanceHealthDto {

    @NotBlank
    public final String status;

    @NotNull
    @Valid
    public final List<HealthCheckResultDto> health_checks;

    public ServiceInstanceHealthDto(@JsonProperty("status") String status,
                                    @JsonProperty("health_checks") List<HealthCheckResultDto> healthChecks) {
        this.status = status;
        this.health_checks = healthChecks;
    }
}
