package org.jgille.mumon.application.report.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public class HealthCheck {

    private final String name;
    private final Status status;
    private final String message;

    private HealthCheck(Builder builder) {
        this.name = builder.name;
        this.status = builder.status;
        this.message = builder.message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String name() {
        return name;
    }

    public Status status() {
        return status;
    }

    public String message() {
        return message;
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

        private String name;
        private Status status;
        private String message;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public HealthCheck build() {
            return new HealthCheck(this);
        }
    }
}
