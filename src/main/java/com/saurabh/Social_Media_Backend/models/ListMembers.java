package com.saurabh.Social_Media_Backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "list_members",uniqueConstraints = {
        @UniqueConstraint(name = "unique_member ",columnNames = {"user_id","list_id"})
})
public class ListMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_member_id")
    private long listMemberId;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "list_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Lists lists;

    @CreationTimestamp
    @Column(name = "added_at",updatable = false,nullable = false)
    private LocalDateTime added_at;

}
