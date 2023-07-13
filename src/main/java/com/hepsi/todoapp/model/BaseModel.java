package com.hepsi.todoapp.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import javax.persistence.*;

@Data
@MappedSuperclass
@Where(clause = "is_deleted = false")
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false)
    private String uuid;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    private Timestamp deletedAt;

    private boolean isDeleted = false;

    private String deletedReason;

    @Version
    private int version;
}
