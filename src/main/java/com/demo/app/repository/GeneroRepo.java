package com.demo.app.repository;

import com.demo.app.model.Genero;
import javax.ejb.Stateless;

/**
 *
 * @author misanchez
 */
@Stateless
public class GeneroRepo extends AbstractRepository<Genero, Integer> {

    public GeneroRepo() {
        super(Genero.class);
    }

}
