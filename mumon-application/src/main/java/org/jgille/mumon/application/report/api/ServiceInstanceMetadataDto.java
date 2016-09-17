package org.jgille.mumon.application.report.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class ServiceInstanceMetadataDto {

    @NotNull
    @Valid
    public final ServiceMetadataDto service;

    @NotNull
    @Valid
    public final InstanceMetadataDto instance;

    @NotNull
    public final Map<String, Object> additional_metadata;

    public ServiceInstanceMetadataDto(@JsonProperty("service") ServiceMetadataDto service,
                                      @JsonProperty("instance") InstanceMetadataDto instance,
                                      @JsonProperty("additional_metadata") Map<String, Object> additionalMetadata) {
        this.service = service;
        this.instance = instance;
        this.additional_metadata = additionalMetadata;
    }
}
