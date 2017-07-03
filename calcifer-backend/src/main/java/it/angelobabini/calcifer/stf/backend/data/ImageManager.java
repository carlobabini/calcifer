package it.angelobabini.calcifer.stf.backend.data;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

import it.angelobabini.calcifer.backend.Setting;

public class ImageManager {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public static boolean existsImage(String name) throws Exception {
		String url = Setting.getAsString("PHOTO_STORAGE_URL") + name; //"ftp://www.angelobabini.it:21/www.angelobabini.it/calcifer/capisaldo/"+name;
		String user = Setting.getAsString("PHOTO_STORAGE_USERNAME"); //"1384980@aruba.it";
		String pass = Setting.getAsString("PHOTO_STORAGE_PASSWORD"); //"b3e85acb27";

		FileSystemManager fsManager = null;
		FileSystemOptions opts = new FileSystemOptions();
		FileObject localFile = null;
		FileObject remoteFile = null;
		if(user != null && user.trim().length() > 0 && pass != null && pass.trim().length() > 0) {
			StaticUserAuthenticator auth = new StaticUserAuthenticator(null, user, pass);
			DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		}
		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

		try {
			fsManager = VFS.getManager();

			remoteFile = fsManager.resolveFile(url, opts);
			//System.out.println(url+" : "+remoteFile.exists());
			return remoteFile.exists();
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

	public static boolean deleteImage(String name) throws Exception {
		String url = Setting.getAsString("PHOTO_STORAGE_URL") + name; //"ftp://www.angelobabini.it:21/www.angelobabini.it/calcifer/capisaldo/"+name;
		String user = Setting.getAsString("PHOTO_STORAGE_USERNAME"); //"1384980@aruba.it";
		String pass = Setting.getAsString("PHOTO_STORAGE_PASSWORD"); //"b3e85acb27";

		FileSystemManager fsManager = null;
		FileSystemOptions opts = new FileSystemOptions();
		FileObject localFile = null;
		FileObject remoteFile = null;
		if(user != null && user.trim().length() > 0 && pass != null && pass.trim().length() > 0) {
			StaticUserAuthenticator auth = new StaticUserAuthenticator(null, user, pass);
			DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		}
		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

		try {
			fsManager = VFS.getManager();

			remoteFile = fsManager.resolveFile(url, opts);
			return remoteFile.delete();
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

	public static File receiveFile(String name) throws Exception {
		return receiveFile(name, File.createTempFile("image_", "jpg"));
	}

	public static File receiveFile(String name, File destination) throws Exception {
		String url = Setting.getAsString("PHOTO_STORAGE_URL") + name; //"ftp://www.angelobabini.it:21/www.angelobabini.it/calcifer/capisaldo/"+name;
		String user = Setting.getAsString("PHOTO_STORAGE_USERNAME"); //"1384980@aruba.it";
		String pass = Setting.getAsString("PHOTO_STORAGE_PASSWORD"); //"b3e85acb27";

		FileSystemManager fsManager = null;
		FileSystemOptions opts = new FileSystemOptions();
		FileObject localFile = null;
		FileObject remoteFile = null;
		if(user != null && user.trim().length() > 0 && pass != null && pass.trim().length() > 0) {
			StaticUserAuthenticator auth = new StaticUserAuthenticator(null, user, pass);
			DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		}
		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

		try {
			fsManager = VFS.getManager();

			localFile = fsManager.resolveFile(destination.getAbsolutePath());
			remoteFile = fsManager.resolveFile(url, opts);

			localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
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
		return destination;
	}

	public static void sendFile(File source, String name) throws Exception {
		String url = Setting.getAsString("PHOTO_STORAGE_URL") + name; //"ftp://www.angelobabini.it:21/www.angelobabini.it/calcifer/capisaldo/"+name;
		String user = Setting.getAsString("PHOTO_STORAGE_USERNAME"); //"1384980@aruba.it";
		String pass = Setting.getAsString("PHOTO_STORAGE_PASSWORD"); //"b3e85acb27";

		FileSystemManager fsManager = null;
		FileSystemOptions opts = new FileSystemOptions();
		FileObject localFile = null;
		FileObject remoteFile = null;
		if(user != null && user.trim().length() > 0 && pass != null && pass.trim().length() > 0) {
			StaticUserAuthenticator auth = new StaticUserAuthenticator(null, user, pass);
			DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		}
		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

		try {
			fsManager = VFS.getManager();

			localFile = fsManager.resolveFile(source.getAbsolutePath());
			remoteFile = fsManager.resolveFile(url, opts);
			
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
	
	public static boolean renameRemoteFile(String oldName, String newName) throws Exception {
		String url = Setting.getAsString("PHOTO_STORAGE_URL") + oldName; //"ftp://www.angelobabini.it:21/www.angelobabini.it/calcifer/capisaldo/"+name;
		String user = Setting.getAsString("PHOTO_STORAGE_USERNAME"); //"1384980@aruba.it";
		String pass = Setting.getAsString("PHOTO_STORAGE_PASSWORD"); //"b3e85acb27";

		FileSystemManager fsManager = null;
		FileSystemOptions opts = new FileSystemOptions();
		FileObject oldFile = null;
		FileObject newFile = null;
		if(user != null && user.trim().length() > 0 && pass != null && pass.trim().length() > 0) {
			StaticUserAuthenticator auth = new StaticUserAuthenticator(null, user, pass);
			DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
		}
		FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
		FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

		try {
			fsManager = VFS.getManager();

			oldFile = fsManager.resolveFile(url, opts);
			newFile = fsManager.resolveFile(Setting.getAsString("PHOTO_STORAGE_URL") + newName, opts);
			
			if(!oldFile.canRenameTo(newFile)) {
				return false;
			} else {
				oldFile.moveTo(newFile);
				return true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				oldFile.close();
			} catch(Exception e) { }
			try {
				newFile.close();
			} catch(Exception e) { }
		}
	}

	public static byte[] fileToBytes(File file, int biggerWidth, int biggerHeight) throws MalformedURLException, IOException {
		if(file==null || !file.exists() || !file.isFile()) {
			return null;
		}
		System.out.println("Photo: "+file.getAbsolutePath());
		BufferedImage image = ImageIO.read(file);
		// resize
		BufferedImage scaled = getScaledImage(image, biggerWidth, biggerHeight);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(scaled, "png", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}

	public static BufferedImage getScaledImage(BufferedImage img, int biggerWidth, int biggerHeight) throws IOException {
		// TODO check System.out.print(img.getWidth()+"x"+img.getHeight()+" vs "+biggerWidth+"x"+biggerHeight);
		float wDiff = (img.getWidth()>biggerWidth ? (float)biggerWidth/img.getWidth() : 0);
		float hDiff = (img.getHeight()>biggerHeight ? (float)biggerHeight/img.getHeight() : 0);
		if(wDiff==0 && hDiff==0) {
			return img;
		}
		//System.out.print(img.getWidth()+"x"+img.getHeight()+" => ");
		int width;
		int height;
		if(wDiff<hDiff) {
			width = biggerWidth;
			height = Math.round((img.getHeight()*biggerWidth)/img.getWidth());
		} else {
			height = biggerHeight;
			width = Math.round((img.getWidth()*biggerHeight)/img.getHeight());
		}
		//System.out.println(width+"x"+height);


		BufferedImage resizedImage = new BufferedImage(width, height, img.getType());
		Graphics2D g = resizedImage.createGraphics();

		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(img, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	}
	
	public static String checkImages(Ricognizione ricognizione) throws Exception {
		String res = "";

		String dataInizio = ricognizione.getInizio() != null ? sdf.format(ricognizione.getInizio()) : "00000000";
		String base = ricognizione.getId()+"_"+dataInizio;

		if(ricognizione.getFoto_manufatto() != null && ricognizione.getFoto_manufatto().length() > 0) {
			if(!existsImage(ricognizione.getFoto_manufatto())) {
				if(existsImage(base+"_manufatto.jpg")) {
					res += "update ricognizioni set foto_manufatto='"+base+"_manufatto.jpg' where instanceid='"+ricognizione.getInstanceID()+"';\n";
				} else {
					res += "-- "+ricognizione.getInstanceID()+"["+ricognizione.getId()+"] "+dataInizio+": "+ricognizione.getFoto_manufatto()+"\n";
				}
			}
		}
		if(ricognizione.getFoto_panoramica() != null && ricognizione.getFoto_panoramica().length() > 0) {
			if(!existsImage(ricognizione.getFoto_panoramica())) {
				if(existsImage(base+"_panoramica.jpg")) {
					res += "update ricognizioni set foto_panoramica='"+base+"_panoramica.jpg' where instanceid='"+ricognizione.getInstanceID()+"';\n";
				} else {
					res += "-- "+ricognizione.getInstanceID()+"["+ricognizione.getId()+"] "+dataInizio+": "+ricognizione.getFoto_panoramica()+"\n";
				}
			}
		}
		if(ricognizione.getFoto_danno_contrassegno() != null && ricognizione.getFoto_danno_contrassegno().length() > 0) {
			if(!existsImage(ricognizione.getFoto_danno_contrassegno())) {
				if(existsImage(base+"_danno_contrassegno.jpg")) {
					res += "update ricognizioni set foto_danno_contrassegno='"+base+"_danno_contrassegno.jpg' where instanceid='"+ricognizione.getInstanceID()+"';\n";
				} else {
					res += "-- "+ricognizione.getInstanceID()+"["+ricognizione.getId()+"] "+dataInizio+": "+ricognizione.getFoto_danno_contrassegno()+"\n";
				}
			}
		}
		if(ricognizione.getFoto_danno_manufatto() != null && ricognizione.getFoto_danno_manufatto().length() > 0) {
			if(!existsImage(ricognizione.getFoto_danno_manufatto())) {
				if(existsImage(base+"_danno_manufatto.jpg")) {
					res += "update ricognizioni set foto_danno_manufatto='"+base+"_danno_manufatto.jpg' where instanceid='"+ricognizione.getInstanceID()+"';\n";
				} else {
					res += "-- "+ricognizione.getInstanceID()+"["+ricognizione.getId()+"] "+dataInizio+": "+ricognizione.getFoto_danno_manufatto()+"\n";
				}
			}
		}
		if(ricognizione.getFoto_aggiornata() != null && ricognizione.getFoto_aggiornata().length() > 0) {
			if(!existsImage(ricognizione.getFoto_aggiornata())) {
				if(existsImage(base+"_aggiornata.jpg")) {
					res += "update ricognizioni set foto_aggiornata='"+base+"_aggiornata.jpg' where instanceid='"+ricognizione.getInstanceID()+"';\n";
				} else {
					res += "-- "+ricognizione.getInstanceID()+"["+ricognizione.getId()+"] "+dataInizio+": "+ricognizione.getFoto_aggiornata()+"\n";
				}
			}
		}
		if(ricognizione.getFoto_sito_ripristino() != null && ricognizione.getFoto_sito_ripristino().length() > 0) {
			if(!existsImage(ricognizione.getFoto_sito_ripristino())) {
				if(existsImage(base+"_sito_ripristino.jpg")) {
					res += "update ricognizioni set foto_sito_ripristino='"+base+"_sito_ripristino.jpg' where instanceid='"+ricognizione.getInstanceID()+"';\n";
				} else {
					res += "-- "+ricognizione.getInstanceID()+"["+ricognizione.getId()+"] "+dataInizio+": "+ricognizione.getFoto_sito_ripristino()+"\n";
				}
			}
		}

		return res;
	}
	
	public static List<String> checkRemoteImages(Ricognizione ricognizione) throws Exception {
		List<String> res = new ArrayList<String>(0);

		String dataInizio = ricognizione.getInizio() != null ? sdf.format(ricognizione.getInizio()) : "00000000";
		String base = ricognizione.getId()+"_"+dataInizio;

		if(ricognizione.getFoto_manufatto() != null && ricognizione.getFoto_manufatto().length() > 0 && !ricognizione.getFoto_manufatto().equals(base+"_manufatto.jpg")) {
			boolean okIMG = ImageManager.renameRemoteFile(ricognizione.getFoto_manufatto(), base+"_manufatto.jpg");
			boolean okDB = false;
			if(okIMG) {
				ricognizione.setFoto_manufatto(base+"_manufatto.jpg");
				okDB = StfDAO.saveRicognizione(ricognizione);
			}
			res.add(ricognizione.getFoto_manufatto() + " => " + base+"_manufatto.jpg" + " : " + (okIMG ? "ok" : "fail") + " IMG , " + (okDB ? "ok" : "fail") + " DB");
		}
		if(ricognizione.getFoto_panoramica() != null && ricognizione.getFoto_panoramica().length() > 0 && !ricognizione.getFoto_panoramica().equals(base+"_panoramica.jpg")) {
			boolean okIMG = ImageManager.renameRemoteFile(ricognizione.getFoto_panoramica(), base+"_panoramica.jpg");
			boolean okDB = false;
			if(okIMG) {
				ricognizione.setFoto_panoramica(base+"_panoramica.jpg");
				okDB = StfDAO.saveRicognizione(ricognizione);
			}
			res.add(ricognizione.getFoto_panoramica() + " => " + base+"_panoramica.jpg" + " : " + (okIMG ? "ok" : "fail") + " IMG , " + (okDB ? "ok" : "fail") + " DB");
		}
		if(ricognizione.getFoto_danno_contrassegno() != null && ricognizione.getFoto_danno_contrassegno().length() > 0 && !ricognizione.getFoto_danno_contrassegno().equals(base+"_danno_contrassegno.jpg")) {
			boolean okIMG = ImageManager.renameRemoteFile(ricognizione.getFoto_danno_contrassegno(), base+"_danno_contrassegno.jpg");
			boolean okDB = false;
			if(okIMG) {
				ricognizione.setFoto_danno_contrassegno(base+"_danno_contrassegno.jpg");
				okDB = StfDAO.saveRicognizione(ricognizione);
			}
			res.add(ricognizione.getFoto_danno_contrassegno() + " => " + base+"_danno_contrassegno.jpg" + " : " + (okIMG ? "ok" : "fail") + " IMG , " + (okDB ? "ok" : "fail") + " DB");
		}
		if(ricognizione.getFoto_danno_manufatto() != null && ricognizione.getFoto_danno_manufatto().length() > 0 && !ricognizione.getFoto_danno_manufatto().equals(base+"_danno_manufatto.jpg")) {
			boolean okIMG = ImageManager.renameRemoteFile(ricognizione.getFoto_danno_manufatto(), base+"_danno_manufatto.jpg");
			boolean okDB = false;
			if(okIMG) {
				ricognizione.setFoto_danno_manufatto(base+"_danno_manufatto.jpg");
				okDB = StfDAO.saveRicognizione(ricognizione);
			}
			res.add(ricognizione.getFoto_danno_manufatto() + " => " + base+"_danno_manufatto.jpg" + " : " + (okIMG ? "ok" : "fail") + " IMG , " + (okDB ? "ok" : "fail") + " DB");
		}
		if(ricognizione.getFoto_aggiornata() != null && ricognizione.getFoto_aggiornata().length() > 0 && !ricognizione.getFoto_aggiornata().equals(base+"_aggiornata.jpg")) {
			boolean okIMG = ImageManager.renameRemoteFile(ricognizione.getFoto_aggiornata(), base+"_aggiornata.jpg");
			boolean okDB = false;
			if(okIMG) {
				ricognizione.setFoto_aggiornata(base+"_aggiornata.jpg");
				okDB = StfDAO.saveRicognizione(ricognizione);
			}
			res.add(ricognizione.getFoto_aggiornata() + " => " + base+"_aggiornata.jpg" + " : " + (okIMG ? "ok" : "fail") + " IMG , " + (okDB ? "ok" : "fail") + " DB");
		}
		if(ricognizione.getFoto_sito_ripristino() != null && ricognizione.getFoto_sito_ripristino().length() > 0 && !ricognizione.getFoto_sito_ripristino().equals(base+"_sito_ripristino.jpg")) {
			boolean okIMG = ImageManager.renameRemoteFile(ricognizione.getFoto_sito_ripristino(), base+"_sito_ripristino.jpg");
			boolean okDB = false;
			if(okIMG) {
				ricognizione.setFoto_sito_ripristino(base+"_sito_ripristino.jpg");
				okDB = StfDAO.saveRicognizione(ricognizione);
			}
			res.add(ricognizione.getFoto_sito_ripristino() + " => " + base+"_sito_ripristino.jpg" + " : " + (okIMG ? "ok" : "fail") + " IMG , " + (okDB ? "ok" : "fail") + " DB");
		}

		return res;
	}
}
