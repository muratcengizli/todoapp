package com.hepsi.todoapp.todoapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RESET_PASSWORD_TOKENS")
public class ResetPasswordToken implements Serializable {
    private static final long serialVersionUID = -6411004890798925042L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String resetPasswordToken;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public ResetPasswordToken(User user) {
        this.user = user;
        this.resetPasswordToken = UUID.randomUUID().toString();
    }
}
