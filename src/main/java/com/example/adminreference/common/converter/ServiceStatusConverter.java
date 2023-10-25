package com.example.adminreference.common.converter;

import com.example.adminreference.enumeration.ServiceStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;

@Converter
public class ServiceStatusConverter implements AttributeConverter<ServiceStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ServiceStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public ServiceStatus convertToEntityAttribute(Integer dbData) {
        return EnumSet.allOf(ServiceStatus.class)
                .stream()
                .filter(e -> e.getCode() == dbData)
                .findAny()
                .orElse(null);
    }
}
