package com.vevs.entity.common;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * Created by Anuj kumar pal
 */
@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;


}
