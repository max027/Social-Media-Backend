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
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tweet_hashtag")
public class Tweet_Hashtag extends BaseEntity{

    @EmbeddedId
    private TweetHashtagId tweet_hash_id;

    @ManyToOne
    @MapsId("tweetsId")
    @JoinColumn(name = "tweet_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tweets tweetsId;


    @ManyToOne(fetch=FetchType.LAZY)
    @MapsId("hashtagId")
    @JoinColumn(name = "hashtag_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private HashTag hashtagId;


}
