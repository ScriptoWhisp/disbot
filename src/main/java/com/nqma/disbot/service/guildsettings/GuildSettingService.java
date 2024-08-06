package com.nqma.disbot.service.guildsettings;

import com.nqma.disbot.repository.GuildSetting;
import com.nqma.disbot.repository.GuildSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GuildSettingService {

    private final GuildSettingRepository guildSettingRepository;

    public Optional<GuildSetting> findGuildSettingById(Long guildId) {
        return guildSettingRepository.findById(guildId);
    }

    public Optional<GuildSetting> addGuildSetting(GuildSetting guildSetting) {
        if (guildSettingRepository.existsById(guildSetting.getGuildId())) {
            return Optional.empty();
        }
        return Optional.of(guildSettingRepository.save(guildSetting));
    }

    public boolean deleteGuildSetting(Long guildId) {
        if (guildSettingRepository.existsById(guildId)) {
            guildSettingRepository.deleteById(guildId);
            return true;
        }
        return false;
    }

    public Optional<GuildSetting> updateGuildSetting(GuildSetting guildSetting) {
        if (guildSettingRepository.existsById(guildSetting.getGuildId())) {
            return Optional.of(guildSettingRepository.save(guildSetting));
        }
        return Optional.empty();
    }
}
