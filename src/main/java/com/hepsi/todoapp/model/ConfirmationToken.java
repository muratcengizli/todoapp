package com.hepsi.todoapp.model;

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
@Table(name = "CONFIRMATION_TOKENS")
public class ConfirmationToken implements Serializable {

    private static final long serialVersionUID = -6918302390318921442L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String confirmationToken;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        this.confirmationToken = UUID.randomUUID().toString();
    }
}
