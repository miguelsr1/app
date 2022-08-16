package com.demo.app.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author DesarrolloPc
 */
@Named
@ApplicationScoped
public class ReportesView {

    private DateFormat FORMAT_DATE_RPT = new SimpleDateFormat("ddMMMyy_HHmmss");

    public void generarReporte(HashMap param) {
        try {
            JasperPrint jasperPrint = getRpt(param, ReportesView.class.getClassLoader().getResourceAsStream(File.separator + "reporte" + File.separator + "rptPersona.jasper"));
            responseRptPdf(jasperPrint, "rptPërsona");
        } catch (IOException | JRException ex) {
            Logger.getLogger(ReportesView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JasperPrint getRpt(HashMap map, InputStream input) {
        JasperPrint jp;
        try {
            try (input) {
                jp = JasperFillManager.fillReport(input, map);
            }
            return jp;
        } catch (JRException | IOException ex) {
            Logger.getLogger(ReportesView.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void responseRptPdf(JasperPrint jp, String nombrePdfGenerado) throws JRException, IOException {
        byte[] content = JasperExportManager.exportReportToPdf(jp);
        downloadFileBytes(content, nombrePdfGenerado, "application/pdf", "pdf");
    }

    public void downloadFileBytes(byte[] outArray, String nombreFile, String tipoDeContenido, String extension) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        response.setContentType(tipoDeContenido);
        response.setContentLength(outArray.length);
        response.setHeader("Content-disposition", "attachment; filename=" + nombreFile + "-" + getFechaGeneracionReporte() + "." + extension);
        try ( OutputStream outStream = response.getOutputStream()) {
            outStream.write(outArray);
            outStream.flush();
        }
        facesContext.responseComplete();
        facesContext.renderResponse();
    }

    public String getFechaGeneracionReporte() {
        return FORMAT_DATE_RPT.format(new Date());
    }
}
