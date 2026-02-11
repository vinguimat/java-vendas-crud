package com.projeto.vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.io.Serializable;

@Entity
@Table(name="state")
public class State implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Preencha este campo")
    private String name;
    private String stabbreviation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStabbreviation() {
        return stabbreviation;
    }

    public void setStabbreviation(String stabbreviation) {
        this.stabbreviation = stabbreviation;
    }
}
