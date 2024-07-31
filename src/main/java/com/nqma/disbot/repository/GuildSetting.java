package com.nqma.disbot.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuildSetting {

    private Long guildId;
    private String channelId;
}
