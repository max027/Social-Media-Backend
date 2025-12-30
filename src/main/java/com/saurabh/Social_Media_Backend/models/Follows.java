package com.saurabh.Social_Media_Backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "follows",
        uniqueConstraints = {
        @UniqueConstraint(name = "unique_bookmark",columnNames = {
                "follower_id","following_id"
        })
})
public class Follows extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follows_id")
    private long followId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "follower_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users followerId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "following_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users followingId;

}
