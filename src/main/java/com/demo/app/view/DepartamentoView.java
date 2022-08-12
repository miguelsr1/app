/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.app.view;

import com.demo.app.model.Departamento;
import com.demo.app.repository.DepartamentoRepo;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author misanchez
 */
@Named
@ViewScoped
public class DepartamentoView implements Serializable {

    @Inject
    private DepartamentoRepo departamentoRepo;

    private String nombreDepartamento;
    private String codigoDepartamento;

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public List<Departamento> getLstDepartamentos() {
        return departamentoRepo.findAll();
    }

}
