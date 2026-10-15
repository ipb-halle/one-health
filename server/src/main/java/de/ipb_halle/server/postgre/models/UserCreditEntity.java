package de.ipb_halle.server.postgre.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_credits")
public class UserCreditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal credit = new BigDecimal("5.00");

    @Column(name = "last_credit_assigned")
    private LocalDateTime lastCreditAssigned;

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public LocalDateTime getLastCreditAssigned() {
        return lastCreditAssigned;
    }

    public void setLastCreditAssigned(LocalDateTime lastCreditAssigned) {
        this.lastCreditAssigned = lastCreditAssigned;
    }
}