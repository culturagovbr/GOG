package br.com.xti.ouvidoria.helper;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.exception.ParsePropertyException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import br.com.xti.ouvidoria.dao.ParametroDAO;
import br.com.xti.ouvidoria.exception.InfrastructureException;

/**
 * @author Samuel Correia Guimarães
 */
public class FileHelper {
	
	public static String getUploadedFilePathname(String uploadedFileName, int manifestationNumber) throws InfrastructureException {
		ParametroDAO parametroDAO = CdiHelper.getFacadeWithJNDI(ParametroDAO.class);
		String attachementDirectory = parametroDAO.getDiretorioAnexo();
    	String normalizedFileName = FileHelper.normalizeName(uploadedFileName);
    	String fullFileName = String.format("%d_%d_%s", manifestationNumber, new Date().getTime(), normalizedFileName);
    	return attachementDirectory.concat(fullFileName);
	}
	
    public static void download(String destFileName, String nomeArquivoDownload) throws IOException, ParsePropertyException, ParsePropertyException, InvalidFormatException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\""+nomeArquivoDownload+"\"");

        // Abre o arquivo gerado
        File relatorio = new File(destFileName);
        response.getOutputStream().write(getFileBytes(relatorio));
        facesContext.responseComplete();
    }
    
    public static void download(File file, String nomeArquivoDownload) throws IOException, ParsePropertyException, ParsePropertyException, InvalidFormatException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment; filename=\""+nomeArquivoDownload+"\"");

        // Abre o arquivo gerado
        response.getOutputStream().write(getFileBytes(file));
        facesContext.responseComplete();
    }
    
    public static String normalizeName(String fileName) {
        return fileName == null ? null : Normalizer.normalize(fileName, Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[-§£$#_'/]", "")
                .replaceAll("\\s+", "_");
    }
    
    private static byte[] getFileBytes(File file) {
    	byte[] bytes = null;
    	try {
        	bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException ex) {
        	System.out.println("Não foi possível encontrar o arquivo: " + file.getAbsolutePath());
            ex.printStackTrace();
        }
    	return bytes;
    }
    
}
