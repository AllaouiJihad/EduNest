package com.jihad.edunest.domaine.entities;

import com.jihad.edunest.domaine.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SchoolRegistrationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String schoolName;
    private String schoolAddress;
    private String schoolPostalCode;
    private String schoolCity;
    private String schoolPhoneNumber;
    private String schoolEmail;
    private String schoolWebsite;
    private String schoolDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String additionalInfo;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private String adminReviewNotes;

    @OneToOne(mappedBy = "registrationRequest", cascade = CascadeType.ALL)
    private School createdSchool;

    @PrePersist
    protected void onCreate() {
        this.submittedAt = LocalDateTime.now();
        this.status = RequestStatus.PENDING;
    }
}
