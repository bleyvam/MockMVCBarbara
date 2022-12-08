package com.minsait.TestingControllers.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
//constructor vacio
@NoArgsConstructor
//constructor con todos los parametros
@AllArgsConstructor
@Entity
@Table(name="bancos")
public class Banco implements Serializable {

    static private final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    //para recombrar el parametro en la BD
    @Column(name = "total_transferencias")
    private  Integer totalTransferencias;




}
