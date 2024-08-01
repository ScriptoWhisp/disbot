package com.nqma.disbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildSettingRepository extends JpaRepository<GuildSetting, Long> {
}
