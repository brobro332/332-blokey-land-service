package xyz.samsami.blokey_land.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class CommonDateTime extends CommonTimestamp {
    @Column(name = "estimated_start_date")
    private LocalDate estimatedStartDate;

    @Column(name = "estimated_end_date")
    private LocalDate estimatedEndDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    public void updateEstimatedStartDate(LocalDate estimatedStartDate) { if (estimatedStartDate != null) this.estimatedStartDate = estimatedStartDate; }
    public void updateEstimatedEndDate(LocalDate estimatedEndDate) { if (estimatedEndDate != null) this.estimatedEndDate = estimatedEndDate; }
    public void updateActualStartDate(LocalDate actualStartDate) { if (actualStartDate != null) this.actualStartDate = actualStartDate; }
    public void updateActualEndDate(LocalDate actualEndDate) { if (actualEndDate != null) this.actualEndDate = actualEndDate; }
}