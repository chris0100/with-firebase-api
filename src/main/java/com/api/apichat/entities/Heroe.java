package com.api.apichat.entities;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "heroe")
public class Heroe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre")
    @Size(min = 3, max = 15)
    private String nombre;

    @NotNull
    @Column(name = "poder")
    private String poder;

    @NotNull
    @Column(name = "vivo")
    private Boolean vivo;
}
