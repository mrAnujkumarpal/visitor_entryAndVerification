package com.vevs.entity.visitor;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class VisitorImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String visitorCode;
    @Lob
    private byte[] photo;
}
