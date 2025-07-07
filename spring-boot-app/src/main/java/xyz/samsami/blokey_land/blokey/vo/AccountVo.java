package xyz.samsami.blokey_land.blokey.vo;

import java.util.List;
import java.util.UUID;

public record AccountVo(UUID accountId, List<String> roles) {
}