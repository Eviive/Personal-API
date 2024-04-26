package com.eviive.personalapi.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Authority {

    READ_PROJECTS("read:projects"),
    CREATE_PROJECTS("create:projects"),
    UPDATE_PROJECTS("update:projects"),
    DELETE_PROJECTS("delete:projects"),

    READ_SKILLS("read:skills"),
    CREATE_SKILLS("create:skills"),
    UPDATE_SKILLS("update:skills"),
    DELETE_SKILLS("delete:skills"),

    REVALIDATE_PORTFOLIO("revalidate:portfolio"),

    READ_ACTUATOR("read:actuator");

    private final String authority;

}
