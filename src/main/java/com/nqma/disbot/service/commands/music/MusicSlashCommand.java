package com.nqma.disbot.service.commands.music;

import com.nqma.disbot.service.commands.SlashCommand;
import discord4j.core.object.entity.Member;

public interface MusicSlashCommand extends SlashCommand {

    void run(Member member, String link);

}
