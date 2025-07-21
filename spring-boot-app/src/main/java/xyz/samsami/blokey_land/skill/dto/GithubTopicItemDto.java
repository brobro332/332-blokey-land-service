package xyz.samsami.blokey_land.skill.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubTopicItemDto(
    String name,
    @JsonProperty("display_name") String displayName
) {}