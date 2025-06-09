package xyz.samsami.blokey_land.common.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class CommonDateTime extends CommonTimestamp{
    private LocalDate startDate;
    private LocalDate endDate;

    public void updateStartDate(LocalDate startDate) { if (startDate != null) this.startDate = startDate; }

    public void updateEndDate(LocalDate endDate) { if (endDate != null) this.endDate = endDate; }
}