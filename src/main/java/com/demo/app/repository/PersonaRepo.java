package com.demo.app.repository;

import com.demo.app.model.Persona;
import javax.ejb.Stateless;

/**
 *
 * @author misanchez
 */
@Stateless
public class PersonaRepo extends AbstractRepository<Persona, Integer> {

    public PersonaRepo() {
        super(Persona.class);
    }
}
