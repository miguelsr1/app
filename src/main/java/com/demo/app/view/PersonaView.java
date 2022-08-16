package com.demo.app.view;

import com.demo.app.model.Departamento;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.SecurityContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
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
    private String codigoDepartamento;
    private Integer idGenero;
    private Integer idMunicipio;
    private HashMap map;

    private Persona persona = new Persona();
    private List<Persona> lstPersonas = new ArrayList();
    private List<Municipio> lstMunicipios = new ArrayList();

    private CroppedImage croppedImage;
    private UploadedFile originalImageFile;
    private StreamedContent content;

    private List<Filtro> params = new ArrayList();

    @Inject
    private SecurityContext securityContext;
    @Inject
    private PersonaRepo personaRepo;
    @Inject
    private MunicipioRepo municipioRepo;
    @Inject
    private CatalagosView catalagosView;
    @Inject
    private ReportesView reporteRepo;

    @ManagedProperty("#{bundle}")
    private ResourceBundle bundle;

    @PostConstruct
    public void init() {
        lstPersonas = personaRepo.findAll();
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

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
        if (persona != null) {
            this.persona = persona;
        }
    }

    public List<Persona> getLstPersonas() {
        return lstPersonas;
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
        if (idMunicipio == null) {
            this.idMunicipio = idMunicipio;
        }
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

    public void onRowSelect(SelectEvent<Persona> event) {
        loadDatosPersona(event.getObject());
    }

    public void editarPersona() {
        loadDatosPersona(persona);
    }

    public void loadDatosPersona(Persona per) {
        idGenero = per.getIdGenero();
        codigoDepartamento = municipioRepo.findByPk(per.getIdMunicipio()).getCodigoDepartamento();
        actulizarMunicipios();
        idMunicipio = per.getIdMunicipio();
        disabled = false;
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
        if (croppedImage != null) {
            persona.setUrlDip(croppedImage.getBytes());

            if (persona.getIdPersona() == null) {
                persona.setFechaInsercion(new Date());
                persona.setEstadoEliminacion((short) 0);
                persona.setUsuarioInsercion(securityContext.getCallerPrincipal().getName());
                personaRepo.save(persona);
                lstPersonas.add(persona);
                JsfUtil.mensajeInsert();
            } else {
                personaRepo.update(persona);
                JsfUtil.mensajeUpdate();
            }

            limpiarForm();
        } else {
            JsfUtil.mensajeError(bundle.getString("msj.error.imagen"));
        }
    }

    public void imprimir() {
        if (persona.getIdPersona() != null) {
            map = new HashMap();
            map.put("pNombres", persona.getPrimerNombre() + " " + persona.getSegundoNombre());
            map.put("pApellidos", persona.getPrimerApellido() + (persona.getSegundoApellido() == null ? (" " + persona.getApellidoCasada()) : (" " + persona.getSegundoApellido())));
            map.put("pDip", persona.getNumeroDip());
            map.put("pNumTelefono", persona.getNumeroTelefono());
            map.put("pNumCelular", persona.getNumeroCelular());
            map.put("pFechaNac", persona.getFechaNacimiento());
            map.put("pDomicilio", persona.getDomicilio());
            map.put("pDepa", catalagosView.getLstDepartamentos().stream().filter(d -> d.getCodigoDepartamento().equals(codigoDepartamento)).findAny().get().getNombreDepartamento());
            map.put("pMuni", lstMunicipios.stream().filter(m -> m.getIdMunicipio().equals(idMunicipio)).findAny().get().getNombreMunicipio());
            map.put("pImagen", persona.getUrlDip());

            reporteRepo.generarReporte(map);
        } else {
            JsfUtil.mensajeAlerta(bundle.getString("msj.reporte.sin_persona"));
        }
    }

    public void eliminar() {
        personaRepo.delete(persona);
        lstPersonas = personaRepo.findAll();
        limpiarForm();
        JsfUtil.mensajeDelete();
    }

    private void limpiarForm() {
        disabled = true;
        persona = new Persona();
        croppedImage = null;
        idGenero = null;
        idMunicipio = null;
        codigoDepartamento = null;
        croppedImage = null;
        originalImageFile = null;
        content = null;
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

    public StreamedContent getFoto(byte[] img) {
        return DefaultStreamedContent.builder()
                .stream(() -> {
                    return new ByteArrayInputStream(img);
                })
                .build();
    }

    public void crop() {
        if (this.croppedImage == null || this.croppedImage.getBytes() == null || this.croppedImage.getBytes().length == 0) {
            JsfUtil.mensajeError(bundle.getString("msj.error.imagen.corte"));
        }
    }

    public CroppedImage getMostrarFoto(byte[] image) {
        CroppedImage im = new CroppedImage();
        im.setBytes(image);
        return im;
    }

}
