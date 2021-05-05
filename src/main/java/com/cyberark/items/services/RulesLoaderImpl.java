package com.cyberark.items.services;

import com.cyberark.items.entities.ItemRuleType;
import com.cyberark.items.entities.ItemType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.Objects;

@Component
public class RulesLoaderImpl implements RulesLoader {

    private final String fileName;
    private final ObjectMapper objectMapper;

    public RulesLoaderImpl(final @Value("${rules-file-name:rules.json}") String fileName,
                           final ObjectMapper objectMapper) {
        this.fileName = fileName;
        this.objectMapper = objectMapper;
    }

    @Override
    @SuppressWarnings("")
    public Map<ItemType, ItemRuleType> getItemRuleMapping() {
        final File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile());
        try {
            return objectMapper.readValue(file, new TypeReference<Map<ItemType, ItemRuleType>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to items orders file", e);
        }
    }
}
