package com.example.adminreference.converter;

import com.example.adminreference.enumeration.UseYn;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UseYnConverter implements AttributeConverter<UseYn, String> {

    @Override
    public String convertToDatabaseColumn(UseYn useYn) {
        if (useYn == null || !useYn.isUsable()) {
            return "0";
        } else {
            return "1";
        }
    }

    @Override
    public UseYn convertToEntityAttribute(String dbData) {
        if ("1".equals(dbData) || "Y".equals(dbData)) {
            return UseYn.Y;
        } else {
            return UseYn.N;
        }
    }
}
