package com.saurabh.Social_Media_Backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetHashtagId implements Serializable {

    @Column(name = "tweet_id")
    private Long tweetsId;

    @Column(name = "hashtag_id")
    private Long hashtagId;

    public boolean equals(Object o){
        if (this==o){
            return true;
        }
        if (!(o instanceof TweetHashtagId)) return false;
        TweetHashtagId that = (TweetHashtagId) o;
        return Objects.equals(tweetsId, that.tweetsId) &&
                Objects.equals(hashtagId, that.hashtagId);

    }
    public int hashCode(){
       return Objects.hash(tweetsId,hashtagId);
    }

}
