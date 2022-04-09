package com.example.cityapi.model;

import com.example.cityapi.enums.State;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

}