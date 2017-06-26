package it.angelobabini.stf_api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.angelobabini.calcifer.backend.Setting;
import it.angelobabini.calcifer.stf.backend.data.ExportImport;
import it.angelobabini.calcifer.stf.backend.data.ImageManager;
import it.angelobabini.calcifer.stf.backend.data.Ricognizione;
import it.angelobabini.calcifer.stf.backend.data.StfDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

@Path("/ricognizioni")
public class RicognizioniREST {
	
	
	@GET
	@Path("/")
	public Response hello() {
		return Response.status(200).entity("ok").build();
	}
	
	@GET
	@Path("/ids")
	@Produces(MediaType.APPLICATION_JSON)
	public String[] getIds() {
		return StfDAO.getRicognzioneUUIDs().toArray(new String[0]);
	}
	
	@GET
	@Path("/syncdata")
	@Produces(MediaType.APPLICATION_JSON)
	public Object[][] getSyncData() {
		return StfDAO.getRicognzioneSyncData();
	}

	// http://localhost:8080/stf-api/rest/ricognizioni/exists/1
	@GET
	@Path("/exists/{param}")
	public Response existsRicognizione(@PathParam("param") String instanceID) {
		Ricognizione ricognizione = StfDAO.getRicognizione(instanceID);
		return Response.status(200).entity(String.valueOf((ricognizione != null))).build();
	}
	
	@GET
	@Path("/get/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ricognizione getRicognizione(@PathParam("param") String instanceid) {
		Ricognizione ricognizione = StfDAO.getRicognizione(instanceid);
		return ricognizione;
	}
	
	@POST
	@Path("/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertRicognizione(Ricognizione ricognizione) {
		boolean result = StfDAO.insertRicognizione(ricognizione);
		return Response.status(HttpStatus.SC_OK).entity(String.valueOf(result)).build();
	}
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRicognizione(Ricognizione ricognizione) {
		boolean result = StfDAO.saveRicognizione(ricognizione);
		return Response.status(HttpStatus.SC_OK).entity(String.valueOf(result)).build();
	}
	
	@GET
	@Path("/backup")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response backup() {
		try {
			File tmpFile = File.createTempFile("", "_backup_ricognizioni.xlsx");
			tmpFile.deleteOnExit();
			ExportImport.exportXLSX(tmpFile);
			InputStream inputStream = new FileInputStream(tmpFile);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String filename = sdf.format(new Date())+"_backup_ricognizioni.xlsx";
			return Response.status(HttpStatus.SC_OK).entity(inputStream).header("Content-Disposition", "attachment;filename="+filename).build();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
	}
	
	@GET
	@Path("/checkRicognizioneImages")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkRicognizioneImages() {
		StringBuilder result = new StringBuilder();
		List<String> ids = StfDAO.getRicognzioneUUIDs();
		for(String id : ids) {
			Ricognizione r = StfDAO.getRicognizione(id);
			try {
				result.append(ImageManager.checkImages(r));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Response.status(HttpStatus.SC_OK).entity(result.toString()).build();
	}
	
	@GET
	@Path("/exportFull2017")
	//@Produces("application/zip")
	@Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	public Response exportFull2017 () {
		FileOutputStream fos = null;
		byte[] buffer = new byte[1024];
		
		File xlsxFile = null;
		File folder = null;
		File zipFile = null;
		Map<String,Map<Integer,Ricognizione>> ricognizioniMap = new HashMap<String,Map<Integer,Ricognizione>>();

    	try {
    		File tmp = File.createTempFile("dummy_", ".tmp");
    		xlsxFile = new File(tmp.getParentFile(), "exportFull2017"+File.separator+"Ricognizioni2017.xlsx");
    		tmp.delete();
    		tmp.deleteOnExit();
    		folder = xlsxFile.getParentFile();
    		folder.mkdirs();

    		// Scelgo le ricognizioni del 2017, eventualmente anche i dati del 2014
    		int i=0;
			Calendar calendar = Calendar.getInstance();
    		List<String> ids = StfDAO.getRicognzioneUUIDs();
    		for(String id : ids) {
    			Ricognizione r = StfDAO.getRicognizione(id);
    			calendar.setTime(r.getInizio());
    			if(calendar.get(Calendar.YEAR) == 2014 || calendar.get(Calendar.YEAR) == 2017) {
    				if(!ricognizioniMap.containsKey(r.getId())) {
    					ricognizioniMap.put(r.getId(), new HashMap<Integer,Ricognizione>());
    				}
    				ricognizioniMap.get(r.getId()).put(calendar.get(Calendar.YEAR), r);
    			}
    			//if(i++ > 3) break;
    		}
    		
    		List<Ricognizione> ricognizioniList = new ArrayList<Ricognizione>();
    		for(String key : ricognizioniMap.keySet()) {
    			Ricognizione r2014 = null;
    			Ricognizione r2017 = null;
    			Map<Integer,Ricognizione> sub = ricognizioniMap.get(key);
    			if(sub.containsKey(2014))
    				r2014 = sub.get(2014);
    			if(sub.containsKey(2017))
    				r2017 = sub.get(2017);

    			if(r2017 != null) {	
	    			if(r2014 != null ) {
	    				if(r2017.getFoto_manufatto() == null || r2017.getFoto_manufatto().length() == 0)
	    					r2017.setFoto_manufatto(r2014.getFoto_manufatto());
	    				if(r2017.getFoto_panoramica() == null || r2017.getFoto_panoramica().length() == 0)
	    					r2017.setFoto_panoramica(r2014.getFoto_panoramica());
	    				r2017.setLongitude(r2014.getLongitude());
	    				r2017.setLatitude(r2014.getLatitude());
	    				r2017.setAltitude(r2014.getAltitude());
	    				r2017.setAccuracy(r2014.getAccuracy());
	    			}
	    			if(r2017.getFoto_manufatto() != null && r2017.getFoto_manufatto().length() > 0) {
	    				//ImageManager.receiveFile(r2017.getFoto_manufatto(), new File(folder, r2017.getFoto_manufatto()));
	    			}
	    			if(r2017.getFoto_panoramica() != null && r2017.getFoto_panoramica().length() > 0) {
	    				//ImageManager.receiveFile(r2017.getFoto_panoramica(), new File(folder, r2017.getFoto_panoramica()));
	    			}
	    			if(r2017.getFoto_danno_contrassegno()!= null && r2017.getFoto_danno_contrassegno().length() > 0) {
	    				//ImageManager.receiveFile(r2017.getFoto_danno_contrassegno(), new File(folder, r2017.getFoto_danno_contrassegno()));
	    			}
	    			if(r2017.getFoto_danno_manufatto()!= null && r2017.getFoto_danno_manufatto().length() > 0) {
	    				//ImageManager.receiveFile(r2017.getFoto_danno_manufatto(), new File(folder, r2017.getFoto_danno_manufatto()));
	    			}
	    			if(r2017.getFoto_aggiornata()!= null && r2017.getFoto_aggiornata().length() > 0) {
	    				//ImageManager.receiveFile(r2017.getFoto_aggiornata(), new File(folder, r2017.getFoto_aggiornata()));
	    			}
	    			if(r2017.getFoto_sito_ripristino() != null && r2017.getFoto_sito_ripristino().length() > 0) {
	    				//ImageManager.receiveFile(r2017.getFoto_sito_ripristino(), new File(folder, r2017.getFoto_sito_ripristino()));
	    			}
	    			ricognizioniList.add(r2017);
	    			
	    			//TODO sostituire i testi nelle combobox con le descrizioni estese
    			}
    		}
    		ExportImport.exportXLSX(xlsxFile, ricognizioniList, Setting.getAsString("PHOTO_BASE_URL"), false);
    		//ExportImport.exportXLSX(xlsxFile, ricognizioniList, "");
    		zipFile = xlsxFile;
    		/*zipFile = new File(folder.getParentFile(), "Ricognizioni2017.zip");
    		zipFile.deleteOnExit();
    		
    		fos = new FileOutputStream(zipFile);
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		
    		for(File f : folder.listFiles()) {
	    		ZipEntry ze = new ZipEntry(f.getName());
	    		zos.putNextEntry(ze);
	    		FileInputStream in = new FileInputStream(f);
	
	    		int len;
	    		while ((len = in.read(buffer)) > 0) {
	    			zos.write(buffer, 0, len);
	    		}
	
	    		in.close();
	    		zos.closeEntry();
    		}

    		//remember close it
    		zos.close();*/

    	}catch(Exception ex){
    	   ex.printStackTrace();
    	   try {
    		   zipFile.delete();
    	   } catch(Exception en) { }
    	}
		return Response.status(HttpStatus.SC_OK).entity(zipFile).build();
	}
	
	@GET
	@Path("/exportGoogleMaps")
	//@Produces("application/zip")
	@Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
	public Response exportGoogleMaps () {
		List<Ricognizione> ricognizioniList = new ArrayList<Ricognizione>();
		List<String> ids = StfDAO.getRicognzioneUUIDs();
		for(String id : ids) {
			ricognizioniList.add(StfDAO.getRicognizione(id));
		}
		try {
			File xlsxFile = File.createTempFile("exportGoogleMaps_", ".xlsx");
			ExportImport.exportXLSX(xlsxFile, ricognizioniList, Setting.getAsString("PHOTO_BASE_URL"), true);
			return Response.status(HttpStatus.SC_OK).entity(xlsxFile).build();
		} catch(Exception e) {
			return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).entity(e.getClass()+" "+e.getMessage()).build();
		}
	}
}