package com.cyberark.items.entities;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class ErrorMessage {
    String message;
    String description;
    int errorCode;
    OffsetDateTime dateTime;
}
