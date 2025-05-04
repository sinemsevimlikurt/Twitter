package com.workintech.twitter.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeId implements Serializable {
    private Long user;
    private Long tweet;

}
