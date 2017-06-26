package it.angelobabini.odkaggregatetools;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class Photo {

	BufferedImage image;
	
	/**
	 * Timeout della connessione, in secondi
	 */
	protected int connectionTimeout = 10;

	/**
	 * Timeout di esecuzione, in secondi. 0 indica un timeout infinito
	 */
	protected int executionTimeout = 0;

	/**
	 * Apre la connessione utilizzando i valori precedentemente impostati
	 *
	 * @throws IOException in caso di errore
	 * @throws EncoderException 
	 * @throws SocketTimeoutException in caso di timeout
	 */
	public void loadRemote(String urlString) throws IOException, EncoderException {
		HttpURLConnection connection = (HttpURLConnection)new URL(urlString).openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		
		// Attendo la risposta
		int rc = connection.getResponseCode();

		// Controllo la risposta
		if(rc==HttpURLConnection.HTTP_OK) {
			image = ImageIO.read(connection.getInputStream());
			File tmp = File.createTempFile("stf_", ".jpg");
			tmp.deleteOnExit();
			ImageIO.write(image, "PNG", tmp);
			FileUtils.copyFile(tmp, new File("C:\\Users\\carbab.VUEMMEIT\\Desktop\\"+tmp.getName()));
		}
		else {
			throw new IOException("HTTP ReturnCode ["+rc+"] not admitted");
		}

		// Chiudo
		connection.disconnect();
	}
	
	public static byte[] urlToBytes(String url, int biggerWidth, int biggerHeight) throws MalformedURLException, IOException {
		if(url==null || url.trim().length()==0) {
			return null;
		}
		System.out.println("Photo: "+url);
		BufferedImage image = ImageIO.read(new URL(url));
		// resize
		BufferedImage scaled = getScaledImage(image, biggerWidth, biggerHeight);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(scaled, "png", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
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
	
	public static BufferedImage bytesToImage(byte[] imageInByte) throws IOException {
		return ImageIO.read(new ByteArrayInputStream(imageInByte));
	}

	/**
	 * Ritorna il valore della propriet� connectionTimeout
	 * @return connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * Imposta il valore della propriet� connectionTimeout
	 * @param connectionTimeout
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * Ritorna il valore della propriet� executionTimeout
	 * @return executionTimeout
	 */
	public int getExecutionTimeout() {
		return executionTimeout;
	}

	/**
	 * Imposta il valore della propriet� executionTimeout
	 * @param executionTimeout
	 */
	public void setExecutionTimeout(int executionTimeout) {
		this.executionTimeout = executionTimeout;
	}
	
	public static void imageToTempFile(byte[] imageInByte, String filename) throws IOException {
		InputStream in = new ByteArrayInputStream(imageInByte);
		imageToTempFile(ImageIO.read(in), filename);
	}
	
	public static void imageToTempFile(BufferedImage image, String filename) throws IOException {
		ImageIO.write(image, "jpg", new File(System.getProperty("java.io.tmpdir")+File.separator+filename));
	}
	
	public static void imageToFile(byte[] imageInByte, File file) throws IOException {
		InputStream in = new ByteArrayInputStream(imageInByte);
		BufferedImage image = ImageIO.read(in);
		ImageIO.write(image, "jpg", file);
	}
	
	public static void imageToFile(byte[] imageInByte, File file, int biggerWidth, int biggerHeight) throws IOException {
		InputStream in = new ByteArrayInputStream(imageInByte);
		BufferedImage scaled = getScaledImage(ImageIO.read(in), biggerWidth, biggerHeight);
		ImageIO.write(scaled, "jpg", file);
	}
}