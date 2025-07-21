package xyz.samsami.blokey_land.skill.scheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.samsami.blokey_land.skill.service.SkillService;

@Component
@RequiredArgsConstructor
public class SkillScheduler {
    private final SkillService skillService;

    @Scheduled(cron = "0 0 1 * * SUN")
    @SchedulerLock(
        name = "SkillScheduler_saveGithubTopics",
        lockAtLeastFor = "PT30S",
        lockAtMostFor = "PT1M"
    )
    @Transactional
    public void saveGithubTopics() {
        skillService.saveTopicsAtoZ();
    }
}