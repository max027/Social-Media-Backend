package com.saurabh.Social_Media_Backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "block",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_bookmark",columnNames = {
                        "blocker_id","blocked_id"
                })
        })
public class Blocked extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private long block_id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "blocker_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users blockerId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "blocked_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users blockedId;


}
