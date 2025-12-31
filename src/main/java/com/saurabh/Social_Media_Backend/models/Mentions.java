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
//change table name
@Table(name = "mentioins",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_mention",columnNames = {
                        "tweets_id","user_id"
                })
        })
public class Mentions extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mention_id")
    private long mentionId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "tweets_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tweets tweets;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users users;

}
