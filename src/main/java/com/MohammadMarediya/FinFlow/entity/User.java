package com.MohammadMarediya.FinFlow.entity;

import com.MohammadMarediya.FinFlow.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role roles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(name = "profile_img_path")
    private String profileImgPath;

<<<<<<< HEAD
=======
    @Column(name = "availableBalance", nullable = false)
    private BigDecimal availableBalance = BigDecimal.ZERO;

>>>>>>> a57c5f76f3b769f078eec88ae44f1c4634f7b55f
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
<<<<<<< HEAD


=======
>>>>>>> a57c5f76f3b769f078eec88ae44f1c4634f7b55f
}
