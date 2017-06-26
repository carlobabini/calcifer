package it.angelobabini.odkaggregatetools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class Remote {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public static Object[][] getList() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(Setting.getAsString("REMOTE_API_BASEPATH")+"/ricognizioni/syncdata");
        Response response = target.request().get();
        //Read the entity
        Object[][] syncData = response.readEntity(Object[][].class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
		}
		
		return syncData;
	}
	
	public static Ricognizione getRicognizione(String uuid) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(Setting.getAsString("REMOTE_API_BASEPATH")+"/ricognizioni/get/"+uuid);
        Response response = target.request().get();
        //Read the entity
        Ricognizione r = response.readEntity(Ricognizione.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
		}
		
		return r;
	}
	
	/*public static void insertRicognizione(RicognizioneIMG ricognizione) throws Exception {
		Ricognizione r = ricognizione.toNewFormat();
		
		String basePath = Setting.getAsString("PHOTO_STORAGE_URL");
		String user = Setting.getAsString("PHOTO_STORAGE_USERNAME");
		String pass = Setting.getAsString("PHOTO_STORAGE_PASSWORD");

		if(ricognizione.getFoto_manufatto() != null && ricognizione.getFoto_manufatto().length > 0) {
			r.setFoto_manufatto(ricognizione.getId() + "_" + sdf.format(ricognizione.getInizio())+"_manufatto.jpg");
			sendFile(basePath + r.getFoto_manufatto(), user, pass, ricognizione.getFoto_manufatto());
		}

		if(ricognizione.getFoto_panoramica() != null && ricognizione.getFoto_panoramica().length > 0) {
			r.setFoto_panoramica(ricognizione.getId() + "_" + sdf.format(ricognizione.getInizio())+"_panoramica.jpg");
			sendFile(basePath + r.getFoto_panoramica(), user, pass, ricognizione.getFoto_panoramica());
		}

		if(ricognizione.getFoto_danno_manufatto() != null && ricognizione.getFoto_danno_manufatto().length > 0) {
			r.setFoto_danno_manufatto(ricognizione.getId() + "_" + sdf.format(ricognizione.getInizio())+"_danno_manufatto.jpg");
			sendFile(basePath + r.getFoto_danno_manufatto(), user, pass, ricognizione.getFoto_danno_manufatto());
		}

		if(ricognizione.getFoto_danno_contrassegno() != null && ricognizione.getFoto_danno_contrassegno().length > 0) {
			r.setFoto_danno_contrassegno(ricognizione.getId() + "_" + sdf.format(ricognizione.getInizio())+"_danno_contrassegno.jpg");
			sendFile(basePath + r.getFoto_danno_contrassegno(), user, pass, ricognizione.getFoto_danno_contrassegno());
		}

		if(ricognizione.getFoto_aggiornata() != null && ricognizione.getFoto_aggiornata().length > 0) {
			r.setFoto_aggiornata(ricognizione.getId() + "_" + sdf.format(ricognizione.getInizio())+"_aggiornata.jpg");
			sendFile(basePath + r.getFoto_aggiornata(), user, pass, ricognizione.getFoto_aggiornata());
		}

		if(ricognizione.getFoto_sito_ripristino() != null && ricognizione.getFoto_sito_ripristino().length > 0) {
			r.setFoto_sito_ripristino(ricognizione.getId() + "_" + sdf.format(ricognizione.getInizio())+"_sito_ripristino.jpg");
			sendFile(basePath + r.getFoto_sito_ripristino(), user, pass, ricognizione.getFoto_sito_ripristino());
		}
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(Setting.getAsString("REMOTE_API_BASEPATH")+"/ricognizioni/insert");
        Entity<Ricognizione> e = Entity.entity(r, MediaType.APPLICATION_JSON);
		Response response = target.request().post(e);

		if (response.getStatus() != 200) {
			throw new Exception("Failed : HTTP error code : "+ response.getStatus());
		}
		
		String responseString = response.readEntity(String.class);
		if(Boolean.parseBoolean(responseString) == false)
			throw new Exception("Failed: response=["+responseString+"]");
	}*/
	
	public static void sendFile(String url, String user, String pass, byte[] filecontent) throws Exception {
		File file = null;
		try {
			file = File.createTempFile("tmp_", ".jpg");
			FileUtils.writeByteArrayToFile(file, filecontent);
			sendFile(url, user, pass, file);
			
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				file.delete();
				file.deleteOnExit();
			} catch(Exception e2) { }
		}
	}
	
	public static void sendFile(String url, String user, String pass, File file) throws Exception {
		FileSystemManager fsManager = null;
        FileSystemOptions opts = new FileSystemOptions();
        FileObject localFile = null;
        FileObject remoteFile = null;
        StaticUserAuthenticator auth = new StaticUserAuthenticator(null, user, pass);
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);

        try {
			fsManager = VFS.getManager();

			localFile = fsManager.resolveFile(file.getAbsolutePath());

			// Create remote file object
			remoteFile = fsManager.resolveFile(url, opts);

			// Copy local file to ftp server
			remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
        } catch (Exception e) {
			throw e;
		} finally {
			try {
				remoteFile.close();
			} catch(Exception e) { }
			try {
				localFile.close();
			} catch(Exception e) { }
		}
	}
	
	public static boolean existsFile(String url, String user, String pass) throws Exception {
		FileSystemManager fsManager = null;
        FileSystemOptions opts = new FileSystemOptions();
        FileObject remoteFile = null;
        StaticUserAuthenticator auth = new StaticUserAuthenticator(null, user, pass);
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);

        try {
			fsManager = VFS.getManager();
			remoteFile = fsManager.resolveFile(url, opts);
			return remoteFile.exists();
        } catch (Exception e) {
			throw e;
		} finally {
			try {
				remoteFile.close();
			} catch(Exception e) { }
		}
	}
}
