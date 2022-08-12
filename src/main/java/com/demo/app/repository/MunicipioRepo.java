package com.demo.app.repository;

import com.demo.app.model.Municipio;
import javax.ejb.Stateless;

/**
 *
 * @author misanchez
 */
@Stateless
public class MunicipioRepo extends AbstractRepository<Municipio, Integer> {

    public MunicipioRepo() {
        super(Municipio.class);
    }
}
