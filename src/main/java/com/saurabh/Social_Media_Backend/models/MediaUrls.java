package com.saurabh.Social_Media_Backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MediaUrls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "tweets_id")
    private Tweets tweets;
}
