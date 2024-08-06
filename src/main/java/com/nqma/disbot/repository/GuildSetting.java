package com.nqma.disbot.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "guild_settings")
public class GuildSetting {

    @Id
    private Long guildId;

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "in_game_mode")
    private boolean inGameMode;
}
