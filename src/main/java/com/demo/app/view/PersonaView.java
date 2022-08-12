package com.demo.app.view;

import com.demo.app.model.Municipio;
import com.demo.app.model.Persona;
import com.demo.app.repository.MunicipioRepo;
import com.demo.app.repository.PersonaRepo;
import com.demo.app.repository.util.Filtro;
import com.demo.app.repository.util.TipoOperador;
import com.demo.app.view.util.JsfUtil;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author misanchez
 */
@Named
@SessionScoped
public class PersonaView implements Serializable {

    private Boolean disabled = true;
    private String nombreDepartamento;
    private String codigoDepartamento;
    private Integer idGenero;
    private Integer idMunicipio;

    private Persona persona = new Persona();
    private List<Municipio> lstMunicipios = new ArrayList();

    private CroppedImage croppedImage;
    private UploadedFile originalImageFile;

    private List<Filtro> params = new ArrayList();

    @Inject
    private PersonaRepo personaRepo;

    @Inject
    private MunicipioRepo municipioRepo;

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Integer getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(Integer idGenero) {
        this.idGenero = idGenero;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

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

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public UploadedFile getOriginalImageFile() {
        return originalImageFile;
    }

    public List<Municipio> getLstMunicipios() {
        return lstMunicipios;
    }

    public void actulizarMunicipios() {
        params.clear();
        params.add(new Filtro(TipoOperador.EQUALS, "codigoDepartamento", codigoDepartamento));
        lstMunicipios = municipioRepo.findListByParam(params, "idMunicipio", false);
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.originalImageFile = null;
        this.croppedImage = null;
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            this.originalImageFile = file;

        }
    }

    public StreamedContent getImage() {
        return DefaultStreamedContent.builder()
                .contentType(originalImageFile == null ? null : originalImageFile.getContentType())
                .stream(() -> {
                    if (originalImageFile == null
                            || originalImageFile.getContent() == null
                            || originalImageFile.getContent().length == 0) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(originalImageFile.getContent());
                    } catch (Exception e) {
                        Logger.getLogger(PersonaView.class.getName()).log(Level.SEVERE, "Imagen Error " + e.getMessage(), e);
                        return null;
                    }
                })
                .build();
    }

    public StreamedContent getCropped() {
        return DefaultStreamedContent.builder()
                .contentType(originalImageFile == null ? null : originalImageFile.getContentType())
                .stream(() -> {
                    if (croppedImage == null
                            || croppedImage.getBytes() == null
                            || croppedImage.getBytes().length == 0) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(this.croppedImage.getBytes());
                    } catch (Exception e) {
                        Logger.getLogger(PersonaView.class.getName()).log(Level.SEVERE, "Cropped error " + e.getMessage(), e);
                        return null;
                    }
                })
                .build();
    }

    public void crop() {
        if (this.croppedImage == null || this.croppedImage.getBytes() == null || this.croppedImage.getBytes().length == 0) {
            JsfUtil.mensajeError("Fallo el corte de la imagen, intentelo nuevamente por favor!");
        }
    }

    public void cambiarOtraFoto() {
        originalImageFile = null;
        croppedImage = null;
    }

    public void nuevo() {
        persona = new Persona();
        disabled = false;
    }

    public void guardar() {
        persona.setIdGenero(idGenero);
        persona.setIdMunicipio(idMunicipio);
        persona.setUrlDip(croppedImage.getBytes());

        if (persona.getIdPersona() == null) {
            personaRepo.save(persona);
            JsfUtil.mensajeInsert();
        } else {
            personaRepo.save(persona);
            JsfUtil.mensajeUpdate();
        }
    }

}
