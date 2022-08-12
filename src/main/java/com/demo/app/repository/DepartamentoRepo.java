package com.demo.app.repository;

import com.demo.app.model.Departamento;
import javax.ejb.Stateless;

/**
 *
 * @author misanchez
 */
@Stateless
public class DepartamentoRepo extends AbstractRepository<Departamento, String>{

    public DepartamentoRepo() {
        super(Departamento.class);
    }
    
}
