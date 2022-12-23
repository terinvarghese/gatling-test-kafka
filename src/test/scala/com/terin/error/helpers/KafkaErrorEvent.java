package com.terin.error.helpers;

import lombok.Builder;

@Builder
public class KafkaErrorEvent {

    private String id;
    private IdType idType;
    private CharSequence application;
    private CharSequence errorCode;
    private CharSequence description;
    private String zonedDateTime;

}
