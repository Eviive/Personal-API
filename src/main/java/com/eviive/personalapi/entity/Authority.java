package com.eviive.personalapi.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Authority {

    READ_PROJECT("read:project"),
    WRITE_PROJECT("write:project"),

    READ_SKILL("read:skill"),
    WRITE_SKILL("write:skill"),

    READ_ACTUATOR("read:actuator");

    private final String authority;

}
