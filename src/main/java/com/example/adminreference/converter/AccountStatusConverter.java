package com.example.adminreference.converter;

import com.example.adminreference.enumeration.AccountState;
import com.example.adminreference.enumeration.UseYn;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.EnumSet;

@Converter
public class AccountStatusConverter implements AttributeConverter<AccountState, String> {
    @Override
    public String convertToDatabaseColumn(AccountState attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public AccountState convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(AccountState.class)
                .stream()
                .filter(e -> e.getCode().equals(dbData))
                .findAny()
                .orElse(null);
    }
}
