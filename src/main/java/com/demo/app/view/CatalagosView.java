package com.demo.app.view;

import com.demo.app.model.Departamento;
import com.demo.app.model.Genero;
import com.demo.app.repository.DepartamentoRepo;
import com.demo.app.repository.GeneroRepo;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author misanchez
 */
@Named
@ApplicationScoped
public class CatalagosView {

    @Inject
    private DepartamentoRepo departamentoRepo;
    @Inject
    private GeneroRepo generoRepo;

    public List<Departamento> getLstDepartamentos() {
        return departamentoRepo.findAll();
    }

    public List<Genero> getLstGenero() {
        return generoRepo.findAll();
    }
}
