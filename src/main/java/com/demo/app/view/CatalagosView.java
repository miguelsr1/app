package com.demo.app.view;

import com.demo.app.model.Departamento;
import com.demo.app.model.Genero;
import com.demo.app.repository.DepartamentoRepo;
import com.demo.app.repository.GeneroRepo;
import com.demo.app.repository.PersonaRepo;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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
    @Inject
    private PersonaRepo personaRepo;

    public List<Departamento> getLstDepartamentos() {
        return departamentoRepo.findAll();
    }

    public List<Genero> getLstGenero() {
        return generoRepo.findAll();
    }

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String id = context.getExternalContext().getRequestParameterMap().get("id");

            return DefaultStreamedContent.builder()
                    .stream(() -> {
                        return new ByteArrayInputStream(personaRepo.findByPk(Integer.parseInt(id)).getUrlDip());
                    })
                    .build();
        }
    }

}
