package com.pastebin.model;

import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@Setter
@RedisHash
public class Views implements Serializable {

    private String hashId;

    private int viewsCount;

    public int getViewsCount() {
        return viewsCount;
    }

    @Id
    public String getHashId() {
        return hashId;
    }

}

