package com.cyberark.items.services;

import com.cyberark.items.entities.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ItemsLoaderImpl implements ItemsLoader {

    private final String fileName;
    private final ObjectMapper objectMapper;

    public ItemsLoaderImpl(@Value("${items-file-name:items.json}") String fileName,
                           final ObjectMapper objectMapper) {
        this.fileName = fileName;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Item> getItems() {
        final File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile());
        try {

            return objectMapper.readValue(file, new TypeReference<List<Item>>() {
            });

        } catch (Exception e) {
            throw new RuntimeException("Error while trying to items orders file", e);
        }
    }

}
