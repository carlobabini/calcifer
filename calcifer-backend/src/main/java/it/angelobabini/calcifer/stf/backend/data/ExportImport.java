package it.angelobabini.calcifer.stf.backend.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import it.angelobabini.calcifer.backend.Setting;

public class ExportImport {

	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static final SimpleDateFormat dateFormatterID = new SimpleDateFormat("yyyyMMdd");

	public static final short POS_INIZIO = 0;
	public static final short POS_FINE = 1;
	public static final short POS_OPERATORE = 2;
	public static final short POS_ID = 3;
	public static final short POS_ID_IN_ALTRE_RETI = 4;
	public static final short POS_ID_CAPOSALDO_PRINCIPALE = 5;
	public static final short POS_LATITUDE = 6;
	public static final short POS_LONGITUDE = 7;
	public static final short POS_ALTITUDE = 8;
	public static final short POS_ACCURACY = 9;
	public static final short POS_UBICAZIONE = 10;
	public static final short POS_INDIRIZZO = 11;
	public static final short POS_ACCESSO = 12;
	public static final short POS_POSIZIONE_CONTRASSEGNO = 13;
	public static final short POS_MATERIALIZZAZIONE = 14;
	public static final short POS_CONTRASSEGNO_ANCORATO = 15;
	public static final short POS_CONTRASSEGNO_DANNEGGIATO = 16;
	public static final short POS_DESCRIZIONE_DANNEGGIAMENTO = 17;
	public static final short POS_TIPOLOGIA_FONDAZIONE = 18;
	public static final short POS_ANOMALIE_MANUFATTO = 19;
	public static final short POS_DESCRIZIONE_ANOMALIE = 20;
	public static final short POS_NOTE_RILEVANTI = 21;
	public static final short POS_DESCR_NOTE_RILEVANTI = 22;
	public static final short POS_ALTRE_NOTE_RILEVANTI = 23;
	public static final short POS_TIPO_CONTESTO_AMBIENTALE = 24;
	public static final short POS_ALTRO_TIPO_CONTESTO_AMBIENTALE = 25;
	public static final short POS_INSTANCEID = 26;
	public static final short POS_MODIFICA = 27;
	public static final short POS_APPARTENENZA = 28;
	public static final short POS_AFFIDABILITA = 29;
	public static final short POS_ESISTENTE = 30;
	public static final short POS_STATO_SCOMPARSO = 31;
	public static final short POS_NOTE_SCOMPARSO = 32;
	public static final short POS_ALTRE_SCOMPARSO = 33;
	public static final short POS_NECESSITA_RIPRISTINO = 34;
	public static final short POS_DESCRIZIONE_RIPRISTINO = 35;
	public static final short POS_LATITUDE_RIPRISTINO = 36;
	public static final short POS_LONGITUDE_RIPRISTINO = 37;
	public static final short POS_ALTITUDE_RIPRISTINO = 38;
	public static final short POS_ACCURACY_RIPRISTINO = 39;
	public static final short POS_IMAGEMANUFATTO = 40;
	public static final short POS_IMAGEPANORAMICA = 41;
	public static final short POS_IMAGEAGGIORNATA = 42;
	public static final short POS_IMAGEDANNO_CONTRASSEGNO = 43;
	public static final short POS_IMAGEDANNO_MANUFATTO = 44;
	public static final short POS_IMAGESITO_RIPRISTINO = 45;

	/*public static File backup() throws IOException, SQLException {
		File csvOutput = null;
		FileWriter csvWriter = null;

		try {
			StringBuilder csv = new StringBuilder();
			csv.append("inizio;");
			csv.append("fine;");
			csv.append("operatore;");
			csv.append("id;");
			csv.append("id_in_altre_reti;");
			csv.append("id_caposaldo_principale;");
			csv.append("latitudine;");
			csv.append("longitudine;");
			csv.append("altitudine;");
			csv.append("accuracy;");
			csv.append("ubicazione;");
			csv.append("indirizzo;");
			csv.append("accesso;");
			csv.append("posizione_contrassegno;");
			csv.append("materializzazione;");
			csv.append("contrassegno_ancorato;");
			csv.append("contrassegno_danneggiato;");
			csv.append("descrizione_danneggiamento;");
			csv.append("tipologia_fondazione;");
			csv.append("anomalie_manufatto;");
			csv.append("descrizione_anomalie;");
			csv.append("note_rilevanti;");
			csv.append("descr_note_rilevanti;");
			csv.append("altre_note_rilevanti;");
			csv.append("tipo;");
			csv.append("altro_tipo;");
			csv.append("instanceid;");
			csv.append("modifica;");
			csv.append("appartenenza;");
			csv.append("affidabilita;");
			csv.append("esistente;");
			csv.append("stato_scomparso;");
			csv.append("note_scomparso;");
			csv.append("altre_scomparso;");
			csv.append("necessita_ripristino;");
			csv.append("descrizione_ripristino;");
			csv.append("latitudine_ripristino;");
			csv.append("longitudine_ripristino;");
			csv.append("altitudine_ripristino;");
			csv.append("accuracy_ripristino;");
			csv.append("imageManufatto;");
			csv.append("imagePanoramica;");
			csv.append("imageAggiornata;");
			csv.append("imageDanno_contrassegno;");
			csv.append("imageDanno_manufatto;");
			csv.append("imageSito_ripristino");
			csv.append("\n");

			List<String> ids = StfDAO.getRicognzioneUUIDs();

			for(String id : ids) {
				Ricognizione r = StfDAO.getRicognizione(id);
				//RicognizioneIMG r = RicognizioneDAO.loadRicognizione(id, true);

				csv.append("\""+(r.getInizio() == null ? "" : dateFormatter.format(r.getInizio()))+"\";");
				csv.append("\""+(r.getFine() == null ? "" : dateFormatter.format(r.getFine()))+"\";");
				csv.append("\""+r.getOperatore()+"\";");
				csv.append("\""+r.getId()+"\";");
				csv.append("\""+r.getId_in_altre_reti()+"\";");
				csv.append("\""+r.getId_caposaldo_principale()+"\";");
				csv.append(String.valueOf(r.getLatitude()).replace(".", ",")+";");
				csv.append(String.valueOf(r.getLongitude()).replace(".", ",")+";");
				csv.append(String.valueOf(r.getAltitude()).replace(".", ",")+";");
				csv.append(String.valueOf(r.getAccuracy()).replace(".", ",")+";");
				csv.append("\""+r.getUbicazione()+"\";");
				csv.append("\""+r.getIndirizzo()+"\";");
				csv.append("\""+r.getAccesso()+"\";");
				csv.append("\""+r.getPosizione_contrassegno()+"\";");
				csv.append("\""+r.getMaterializzazione()+"\";");
				csv.append("\""+r.getContrassegno_ancorato()+"\";");
				csv.append("\""+r.getContrassegno_danneggiato()+"\";");
				csv.append("\""+r.getDescrizione_danneggiamento()+"\";");
				csv.append("\""+r.getTipologia_fondazione()+"\";");
				csv.append("\""+r.getAnomalie_manufatto()+"\";");
				csv.append("\""+r.getDescrizione_anomalie()+"\";");
				csv.append("\""+r.getNote_rilevanti()+"\";");
				csv.append("\""+r.getDescr_note_rilevanti()+"\";");
				csv.append("\""+r.getAltre_note_rilevanti()+"\";");
				csv.append("\""+r.getTipo_contesto_ambientale()+"\";");
				csv.append("\""+r.getAltro_tipo_contesto_ambientale()+"\";");
				csv.append("\""+r.getInstanceID()+"\";");
				csv.append("\""+(r.getModifica() == null ? "" : dateFormatter.format(r.getModifica()))+"\";");
				csv.append("\""+r.getAppartenenza()+"\";");
				csv.append(String.valueOf(r.getAffidabilita())+";");
				csv.append("\""+r.getEsistente()+"\";");
				csv.append("\""+r.getStato_scomparso()+"\";");
				csv.append("\""+r.getNote_scomparso()+"\";");
				csv.append("\""+r.getAltre_scomparso()+"\";");
				csv.append("\""+r.getNecessita_ripristino()+"\";");
				csv.append("\""+r.getDescrizione_ripristino()+"\";");
				csv.append(String.valueOf(r.getLatitude_ripristino()).replace(".", ",")+";");
				csv.append(String.valueOf(r.getLongitude_ripristino()).replace(".", ",")+";");
				csv.append(String.valueOf(r.getAltitude_ripristino()).replace(".", ",")+";");
				csv.append(String.valueOf(r.getAccuracy_ripristino()).replace(".", ",")+";");
				String foto = r.getFoto_manufatto();
				if(foto!=null && foto.trim().length() > 0) {
					csv.append("\""+"http://www.angelobabini.it/calcifer/capisaldo/"+foto+"\";");
				} else {
					csv.append("\"\";");
				}
				foto = r.getFoto_panoramica();
				if(foto!=null && foto.trim().length() > 0) {
					csv.append("\""+"http://www.angelobabini.it/calcifer/capisaldo/"+foto+"\";");
				} else {
					csv.append("\"\";");
				}
				foto = r.getFoto_aggiornata();
				if(foto!=null && foto.trim().length() > 0) {
					csv.append("\""+"http://www.angelobabini.it/calcifer/capisaldo/"+foto+"\";");
				} else {
					csv.append("\"\";");
				}
				foto = r.getFoto_danno_contrassegno();
				if(foto!=null && foto.trim().length() > 0) {
					csv.append("\""+"http://www.angelobabini.it/calcifer/capisaldo/"+foto+"\";");
				} else {
					csv.append("\"\";");
				}
				foto = r.getFoto_danno_manufatto();
				if(foto!=null && foto.trim().length() > 0) {
					csv.append("\""+"http://www.angelobabini.it/calcifer/capisaldo/"+foto+"\";");
				} else {
					csv.append("\"\";");
				}
				foto = r.getFoto_sito_ripristino();
				if(foto!=null && foto.trim().length() > 0) {
					csv.append("\""+"http://www.angelobabini.it/calcifer/capisaldo/"+foto+"\";");
				} else {
					csv.append("\"\"");
				}
				csv.append("\n");

				r = null;

			}

			csvOutput = File.createTempFile("backup_ricognizioni_", ".csv");
			csvWriter = new FileWriter(csvOutput);
			csvWriter.write(csv.toString());

		} finally {
			if(csvWriter!=null) {
				try {
					csvWriter.close();
				} catch(Exception e) { e.printStackTrace(); }
			}
		}
		return csvOutput;
	}*/

	public static synchronized String importXLSX(File fileXLSX, String username) throws Exception {
		StringBuilder sb = new StringBuilder();
		XSSFWorkbook workbook = null;
		int modifiche = 0;
		int scartati = 0;
		int errori = 0;

		try {
			workbook = new XSSFWorkbook(fileXLSX);
			Sheet sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());

			System.out.println("Inizio importazione");
			Row row = null;
			for(int i=sheet.getFirstRowNum() + 1; i<=sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if(row != null) {
					Ricognizione input = new Ricognizione();
					try {
						input.setInizio(readTimestamp(row.getCell(POS_INIZIO)));
						input.setFine(readTimestamp(row.getCell(POS_FINE)));
						input.setOperatore(readString(row.getCell(POS_OPERATORE)));
						input.setId(readString(row.getCell(POS_ID)));
						input.setId_in_altre_reti(readString(row.getCell(POS_ID_IN_ALTRE_RETI)));
						input.setId_caposaldo_principale(readString(row.getCell(POS_ID_CAPOSALDO_PRINCIPALE)));
						input.setLatitude(readDouble(row.getCell(POS_LATITUDE)));
						input.setLongitude(readDouble(row.getCell(POS_LONGITUDE)));
						input.setAltitude(readDouble(row.getCell(POS_ALTITUDE)));
						input.setAccuracy(readDouble(row.getCell(POS_ACCURACY)));
						input.setUbicazione(readString(row.getCell(POS_UBICAZIONE)));
						input.setIndirizzo(readString(row.getCell(POS_INDIRIZZO)));
						input.setAccesso(readString(row.getCell(POS_ACCESSO)));
						input.setPosizione_contrassegno(readString(row.getCell(POS_POSIZIONE_CONTRASSEGNO)));
						input.setMaterializzazione(readString(row.getCell(POS_MATERIALIZZAZIONE)));
						input.setContrassegno_ancorato(readString(row.getCell(POS_CONTRASSEGNO_ANCORATO)));
						input.setContrassegno_danneggiato(readString(row.getCell(POS_CONTRASSEGNO_DANNEGGIATO)));
						input.setDescrizione_danneggiamento(readString(row.getCell(POS_DESCRIZIONE_DANNEGGIAMENTO)));
						input.setTipologia_fondazione(readString(row.getCell(POS_TIPOLOGIA_FONDAZIONE)));
						input.setAnomalie_manufatto(readString(row.getCell(POS_ANOMALIE_MANUFATTO)));
						input.setDescrizione_anomalie(readString(row.getCell(POS_DESCRIZIONE_ANOMALIE)));
						input.setNote_rilevanti(readString(row.getCell(POS_NOTE_RILEVANTI)));
						input.setDescr_note_rilevanti(readString(row.getCell(POS_DESCR_NOTE_RILEVANTI)));
						input.setAltre_note_rilevanti(readString(row.getCell(POS_ALTRE_NOTE_RILEVANTI)));
						input.setTipo_contesto_ambientale(readString(row.getCell(POS_TIPO_CONTESTO_AMBIENTALE)));
						input.setAltro_tipo_contesto_ambientale(readString(row.getCell(POS_ALTRO_TIPO_CONTESTO_AMBIENTALE)));
						input.setInstanceID(readString(row.getCell(POS_INSTANCEID)));
						input.setModifica(readTimestamp(row.getCell(POS_MODIFICA)));
						input.setAppartenenza(readString(row.getCell(POS_APPARTENENZA)));
						input.setEsistente(readString(row.getCell(POS_ESISTENTE)));
						input.setStato_scomparso(readString(row.getCell(POS_STATO_SCOMPARSO)));
						input.setNote_scomparso(readString(row.getCell(POS_NOTE_SCOMPARSO)));
						input.setAltre_scomparso(readString(row.getCell(POS_ALTRE_SCOMPARSO)));
						input.setNecessita_ripristino(readString(row.getCell(POS_NECESSITA_RIPRISTINO)));
						input.setDescrizione_ripristino(readString(row.getCell(POS_DESCRIZIONE_RIPRISTINO)));
						input.setLatitude_ripristino(readDouble(row.getCell(POS_LATITUDE_RIPRISTINO)));
						input.setLongitude_ripristino(readDouble(row.getCell(POS_LONGITUDE_RIPRISTINO)));
						input.setAltitude_ripristino(readDouble(row.getCell(POS_ALTITUDE_RIPRISTINO)));
						input.setAccuracy_ripristino(readDouble(row.getCell(POS_ACCURACY_RIPRISTINO)));
						//input.setimageManufatto(readString(row.getCell(POS_IMAGEMANUFATTO)));
						//input.setimagePanoramica(readString(row.getCell(POS_IMAGEPANORAMICA)));
						//input.setimageAggiornata(readString(row.getCell(POS_IMAGEAGGIORNATA)));
						//input.setimageDanno_contrassegno(readString(row.getCell(POS_IMAGEDANNO_CONTRASSEGNO)));
						//input.setimageDanno_manufatto(readString(row.getCell(POS_IMAGEDANNO_MANUFATTO)));
						//input.setimageSito_ripristino(readString(row.getCell(POS_IMAGESITO_RIPRISTINO)));


						Ricognizione ricognizione = StfDAO.getRicognizione(readString(row.getCell(POS_INSTANCEID)));
						if(ricognizione == null) {
							scartati++;
						} else {
							boolean modified = false;
							modified = modified || differentStrings(ricognizione.getOperatore(), input.getOperatore());
							modified = modified || differentStrings(ricognizione.getId_in_altre_reti(), input.getId_in_altre_reti());
							modified = modified || differentStrings(ricognizione.getId_caposaldo_principale(), input.getId_caposaldo_principale());
							modified = modified || ricognizione.getLatitude() != input.getLatitude();
							modified = modified || ricognizione.getLongitude() !=input.getLongitude();
							modified = modified || ricognizione.getAltitude() !=input.getAltitude();
							modified = modified || ricognizione.getAccuracy() != input.getAccuracy();
							modified = modified || differentStrings(ricognizione.getUbicazione(), input.getUbicazione());
							modified = modified || differentStrings(ricognizione.getIndirizzo(), input.getIndirizzo());
							modified = modified || differentStrings(ricognizione.getAccesso(), input.getAccesso());
							modified = modified || differentStrings(ricognizione.getPosizione_contrassegno(), input.getPosizione_contrassegno());
							modified = modified || differentStrings(ricognizione.getMaterializzazione(), input.getMaterializzazione());
							modified = modified || differentStrings(ricognizione.getContrassegno_ancorato(), input.getContrassegno_ancorato());
							modified = modified || differentStrings(ricognizione.getContrassegno_danneggiato(), input.getContrassegno_danneggiato());
							modified = modified || differentStrings(ricognizione.getDescrizione_danneggiamento(), input.getDescrizione_danneggiamento());
							modified = modified || differentStrings(ricognizione.getTipologia_fondazione(), input.getTipologia_fondazione());
							modified = modified || differentStrings(ricognizione.getAnomalie_manufatto(), input.getAnomalie_manufatto());
							modified = modified || differentStrings(ricognizione.getDescrizione_anomalie(), input.getDescrizione_anomalie());
							modified = modified || differentStrings(ricognizione.getNote_rilevanti(), input.getNote_rilevanti());
							modified = modified || differentStrings(ricognizione.getDescr_note_rilevanti(), input.getDescr_note_rilevanti());
							modified = modified || differentStrings(ricognizione.getAltre_note_rilevanti(), input.getAltre_note_rilevanti());
							modified = modified || differentStrings(ricognizione.getTipo_contesto_ambientale(), input.getTipo_contesto_ambientale());
							modified = modified || differentStrings(ricognizione.getAltro_tipo_contesto_ambientale(), input.getAltro_tipo_contesto_ambientale());
							modified = modified || differentStrings(ricognizione.getAppartenenza(), input.getAppartenenza());
							modified = modified || differentStrings(ricognizione.getEsistente(), input.getEsistente());
							modified = modified || differentStrings(ricognizione.getStato_scomparso(), input.getStato_scomparso());
							modified = modified || differentStrings(ricognizione.getNote_scomparso(), input.getNote_scomparso());
							modified = modified || differentStrings(ricognizione.getAltre_scomparso(), input.getAltre_scomparso());
							modified = modified || differentStrings(ricognizione.getNecessita_ripristino(), input.getNecessita_ripristino());
							modified = modified || differentStrings(ricognizione.getDescrizione_ripristino(), input.getDescrizione_ripristino());
							modified = modified || ricognizione.getLatitude_ripristino() !=input.getLatitude_ripristino();
							modified = modified || ricognizione.getLongitude_ripristino() !=input.getLongitude_ripristino();
							modified = modified || ricognizione.getLongitude_ripristino() !=input.getLongitude_ripristino();
							modified = modified || ricognizione.getAccuracy_ripristino() !=input.getAccuracy_ripristino();

							if(!modified) {
								scartati++;
							} else {
								StfDAO.saveRicognizioneLog(ricognizione, username, "modifica");

								input.setInizio(ricognizione.getInizio());
								input.setFine(ricognizione.getFine());
								input.setId(ricognizione.getId());
								input.setId_in_altre_reti(ricognizione.getId_in_altre_reti());
								input.setId_caposaldo_principale(ricognizione.getId_caposaldo_principale());
								input.setFoto_manufatto(ricognizione.getFoto_manufatto());
								input.setFoto_panoramica(ricognizione.getFoto_panoramica());
								input.setFoto_aggiornata(ricognizione.getFoto_aggiornata());
								input.setFoto_danno_contrassegno(ricognizione.getFoto_danno_contrassegno());
								input.setFoto_danno_manufatto(ricognizione.getFoto_danno_manufatto());
								input.setFoto_sito_ripristino(ricognizione.getFoto_sito_ripristino());
								input.setModifica(new Timestamp(new Date().getTime()));
								StfDAO.saveRicognizione(input);

								modifiche++;
								System.out.println("Modificata ricognizione "+input.getInstanceID()+" ["+input.getId()+"]");
							}
						}
					} catch(Exception e) {
						e.printStackTrace();
						sb.append("Errore modifica istanza "+input.getInstanceID()+": "+e.getClass()+" "+e.getMessage()+"\n");
						errori++;
					}
					System.out.println("Fine importazione");
				}
			}
			sb.append("Modificate "+modifiche+"\nScartati "+scartati+"\nErrori "+errori+"\n");
		} catch(Exception e) {
			sb.append("Errore: "+e.getClass()+" "+e.getMessage());
			throw e;
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private static String readString(Cell cell) {
		String s = "";
		if(cell != null) {
			switch(cell.getCellTypeEnum()) {
			case STRING: {
				s = cell.getStringCellValue();
				break;
			}
			case NUMERIC: {
				s = String.valueOf(cell.getNumericCellValue());
				break;
			}
			case BOOLEAN: {
				s = String.valueOf(cell.getBooleanCellValue());
				break;
			}
			case BLANK: {
				s = "";
				break;
			}
			case FORMULA: {
				s = "";
				break;
			}
			default:
				s = "";
			}
		}
		return s;
	}

	private static double readDouble(Cell cell) {
		Double d = 0.0;
		if(cell != null) {
			switch(cell.getCellTypeEnum()) {
			case STRING: {
				try {
					d = Double.parseDouble(cell.getStringCellValue());
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case NUMERIC: {
				d = cell.getNumericCellValue();
				break;
			}
			case BOOLEAN: {
				d = 0.0;
				break;
			}
			case BLANK: {
				d = 0.0;
				break;
			}
			case FORMULA: {
				d = 0.0;
				break;
			}
			default:
				d = 0.0;
			}
		}
		return d;
	}

	private static Timestamp readTimestamp(Cell cell) {
		Timestamp t = null;
		if(cell != null) {
			switch(cell.getCellTypeEnum()) {
			case STRING: {
				try {
					t = Timestamp.valueOf(cell.getStringCellValue());
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case NUMERIC: {
				t = new Timestamp(cell.getDateCellValue().getTime());
				break;
			}
			case BOOLEAN: {
				t = null;
				break;
			}
			case BLANK: {
				t = null;
				break;
			}
			case FORMULA: {
				t = null;
				break;
			}
			default:
				t = null;
			}
		}
		return t;
	}

	private static boolean differentStrings(String a, String b) {
		if(a == null && b == null)
			return true;
		if((a != null && b == null) || (a == null && b != null))
			return true;
		return !a.equals(b);
	}

	public static synchronized void exportXLSX(File fileXLSX) throws Exception {
		SXSSFWorkbook workbook = null;
		XSSFWorkbook _XSSFWorkbook = null;
		
		String baseUrl = Setting.getAsString("PHOTO_BASE_URL");
		
		try {
			_XSSFWorkbook = new XSSFWorkbook();
			workbook = new SXSSFWorkbook(_XSSFWorkbook, 2);
			SXSSFSheet sheet = workbook.createSheet(fileXLSX.getName().replaceAll(".xlsx", ""));
			
			DataFormat fmt = workbook.createDataFormat();
			CellStyle text_style = workbook.createCellStyle();
			text_style.setDataFormat(fmt.getFormat("@"));
			sheet.setDefaultColumnStyle(3, text_style);
			
			CellStyle hlink_style = workbook.createCellStyle();
	        Font hlink_font = workbook.createFont();
	        hlink_font.setUnderline(Font.U_SINGLE);
	        hlink_font.setColor(IndexedColors.BLUE.getIndex());
	        hlink_style.setFont(hlink_font);
	        
	        CellStyle date_style = workbook.createCellStyle();
			date_style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("m/d/yy h:mm"));

			SXSSFRow row = sheet.createRow(0);
			row.createCell(POS_INIZIO, CellType.STRING).setCellValue("Inizio");
			row.createCell(POS_FINE, CellType.STRING).setCellValue("Fine");
			row.createCell(POS_OPERATORE, CellType.STRING).setCellValue("Operatore");
			row.createCell(POS_ID, CellType.STRING).setCellValue("Id");
			row.createCell(POS_ID_IN_ALTRE_RETI, CellType.STRING).setCellValue("Id_In_Altre_Reti");
			row.createCell(POS_ID_CAPOSALDO_PRINCIPALE, CellType.STRING).setCellValue("Id_Caposaldo_Principale");
			row.createCell(POS_LATITUDE, CellType.STRING).setCellValue("Latitude");
			row.createCell(POS_LONGITUDE, CellType.STRING).setCellValue("Longitude");
			row.createCell(POS_ALTITUDE, CellType.STRING).setCellValue("Altitude");
			row.createCell(POS_ACCURACY, CellType.STRING).setCellValue("Accuracy");
			row.createCell(POS_UBICAZIONE, CellType.STRING).setCellValue("Ubicazione");
			row.createCell(POS_INDIRIZZO, CellType.STRING).setCellValue("Indirizzo");
			row.createCell(POS_ACCESSO, CellType.STRING).setCellValue("Accesso");
			row.createCell(POS_POSIZIONE_CONTRASSEGNO, CellType.STRING).setCellValue("Posizione_Contrassegno");
			row.createCell(POS_MATERIALIZZAZIONE, CellType.STRING).setCellValue("Materializzazione");
			row.createCell(POS_CONTRASSEGNO_ANCORATO, CellType.STRING).setCellValue("Contrassegno_Ancorato");
			row.createCell(POS_CONTRASSEGNO_DANNEGGIATO, CellType.STRING).setCellValue("Contrassegno_Danneggiato");
			row.createCell(POS_DESCRIZIONE_DANNEGGIAMENTO, CellType.STRING).setCellValue("Descrizione_Danneggiamento");
			row.createCell(POS_TIPOLOGIA_FONDAZIONE, CellType.STRING).setCellValue("Tipologia_Fondazione");
			row.createCell(POS_ANOMALIE_MANUFATTO, CellType.STRING).setCellValue("Anomalie_Manufatto");
			row.createCell(POS_DESCRIZIONE_ANOMALIE, CellType.STRING).setCellValue("Descrizione_Anomalie");
			row.createCell(POS_NOTE_RILEVANTI, CellType.STRING).setCellValue("Note_Rilevanti");
			row.createCell(POS_DESCR_NOTE_RILEVANTI, CellType.STRING).setCellValue("Descr_Note_Rilevanti");
			row.createCell(POS_ALTRE_NOTE_RILEVANTI, CellType.STRING).setCellValue("Altre_Note_Rilevanti");
			row.createCell(POS_TIPO_CONTESTO_AMBIENTALE, CellType.STRING).setCellValue("Tipo_Contesto_Ambientale");
			row.createCell(POS_ALTRO_TIPO_CONTESTO_AMBIENTALE, CellType.STRING).setCellValue("Altro_Tipo_Contesto_Ambientale");
			row.createCell(POS_INSTANCEID, CellType.STRING).setCellValue("Instanceid");
			row.createCell(POS_MODIFICA, CellType.STRING).setCellValue("Modifica");
			row.createCell(POS_APPARTENENZA, CellType.STRING).setCellValue("Appartenenza");
			row.createCell(POS_AFFIDABILITA, CellType.STRING).setCellValue("Affidabilita");
			row.createCell(POS_ESISTENTE, CellType.STRING).setCellValue("Esistente");
			row.createCell(POS_STATO_SCOMPARSO, CellType.STRING).setCellValue("Stato_Scomparso");
			row.createCell(POS_NOTE_SCOMPARSO, CellType.STRING).setCellValue("Note_Scomparso");
			row.createCell(POS_ALTRE_SCOMPARSO, CellType.STRING).setCellValue("Altre_Scomparso");
			row.createCell(POS_NECESSITA_RIPRISTINO, CellType.STRING).setCellValue("Necessita_Ripristino");
			row.createCell(POS_DESCRIZIONE_RIPRISTINO, CellType.STRING).setCellValue("Descrizione_Ripristino");
			row.createCell(POS_LATITUDE_RIPRISTINO, CellType.STRING).setCellValue("Latitude_Ripristino");
			row.createCell(POS_LONGITUDE_RIPRISTINO, CellType.STRING).setCellValue("Longitude_Ripristino");
			row.createCell(POS_ALTITUDE_RIPRISTINO, CellType.STRING).setCellValue("Altitude_Ripristino");
			row.createCell(POS_ACCURACY_RIPRISTINO, CellType.STRING).setCellValue("Accuracy_Ripristino");
			row.createCell(POS_IMAGEMANUFATTO, CellType.STRING).setCellValue("Image Manufatto");
			row.createCell(POS_IMAGEPANORAMICA, CellType.STRING).setCellValue("Image Panoramica");
			row.createCell(POS_IMAGEAGGIORNATA, CellType.STRING).setCellValue("Image Aggiornata");
			row.createCell(POS_IMAGEDANNO_CONTRASSEGNO, CellType.STRING).setCellValue("imageDanno_Contrassegno");
			row.createCell(POS_IMAGEDANNO_MANUFATTO, CellType.STRING).setCellValue("imageDanno_Manufatto");
			row.createCell(POS_IMAGESITO_RIPRISTINO, CellType.STRING).setCellValue("imageSito_Ripristino");

			int i = 1;
			List<String> ids = StfDAO.getRicognzioneUUIDs();
			for(String id : ids) {
				row = sheet.createRow(i);
				i++;
				Ricognizione r = StfDAO.getRicognizione(id);
				dateCell(row, workbook, date_style, POS_INIZIO, r.getInizio());
				dateCell(row, workbook, date_style, POS_FINE, r.getFine());
				row.createCell(POS_OPERATORE, CellType.STRING).setCellValue(r.getOperatore());
				row.createCell(POS_ID, CellType.STRING).setCellValue(r.getId());
				row.getCell(POS_ID).setCellType(CellType.STRING);
				row.getCell(POS_ID).setCellStyle(text_style);
				row.createCell(POS_ID_IN_ALTRE_RETI, CellType.STRING).setCellValue(r.getId_in_altre_reti());
				row.getCell(POS_ID_IN_ALTRE_RETI).setCellType(CellType.STRING);
				row.getCell(POS_ID_IN_ALTRE_RETI).setCellStyle(text_style);
				row.createCell(POS_ID_CAPOSALDO_PRINCIPALE, CellType.STRING).setCellValue(r.getId_caposaldo_principale());
				row.getCell(POS_ID_CAPOSALDO_PRINCIPALE).setCellType(CellType.STRING);
				row.getCell(POS_ID_CAPOSALDO_PRINCIPALE).setCellStyle(text_style);
				row.createCell(POS_LATITUDE, CellType.NUMERIC).setCellValue(r.getLatitude());
				row.createCell(POS_LONGITUDE, CellType.NUMERIC).setCellValue(r.getLongitude());
				row.createCell(POS_ALTITUDE, CellType.NUMERIC).setCellValue(r.getAltitude());
				row.createCell(POS_ACCURACY, CellType.NUMERIC).setCellValue(r.getAccuracy());
				row.createCell(POS_UBICAZIONE, CellType.STRING).setCellValue(r.getUbicazione());
				row.createCell(POS_INDIRIZZO, CellType.STRING).setCellValue(r.getIndirizzo());
				row.createCell(POS_ACCESSO, CellType.STRING).setCellValue(r.getAccesso());
				row.createCell(POS_POSIZIONE_CONTRASSEGNO, CellType.STRING).setCellValue(r.getPosizione_contrassegno());
				row.createCell(POS_MATERIALIZZAZIONE, CellType.STRING).setCellValue(r.getMaterializzazione());
				row.createCell(POS_CONTRASSEGNO_ANCORATO, CellType.STRING).setCellValue(r.getContrassegno_ancorato());
				row.createCell(POS_CONTRASSEGNO_DANNEGGIATO, CellType.STRING).setCellValue(r.getContrassegno_danneggiato());
				row.createCell(POS_DESCRIZIONE_DANNEGGIAMENTO, CellType.STRING).setCellValue(r.getDescrizione_danneggiamento());
				row.createCell(POS_TIPOLOGIA_FONDAZIONE, CellType.STRING).setCellValue(r.getTipologia_fondazione());
				row.createCell(POS_ANOMALIE_MANUFATTO, CellType.STRING).setCellValue(r.getAnomalie_manufatto());
				row.createCell(POS_DESCRIZIONE_ANOMALIE, CellType.STRING).setCellValue(r.getDescrizione_anomalie());
				row.createCell(POS_NOTE_RILEVANTI, CellType.STRING).setCellValue(r.getNote_rilevanti());
				row.createCell(POS_DESCR_NOTE_RILEVANTI, CellType.STRING).setCellValue(r.getDescr_note_rilevanti());
				row.createCell(POS_ALTRE_NOTE_RILEVANTI, CellType.STRING).setCellValue(r.getAltre_note_rilevanti());
				row.createCell(POS_TIPO_CONTESTO_AMBIENTALE, CellType.STRING).setCellValue(r.getTipo_contesto_ambientale());
				row.createCell(POS_ALTRO_TIPO_CONTESTO_AMBIENTALE, CellType.STRING).setCellValue(r.getAltro_tipo_contesto_ambientale());
				row.createCell(POS_INSTANCEID, CellType.STRING).setCellValue(r.getInstanceID());
				dateCell(row, workbook, date_style, POS_MODIFICA, r.getModifica());
				row.createCell(POS_APPARTENENZA, CellType.STRING).setCellValue(r.getAppartenenza());
				row.createCell(POS_AFFIDABILITA, CellType.NUMERIC).setCellValue(r.getAffidabilita());
				row.createCell(POS_ESISTENTE, CellType.STRING).setCellValue(r.getEsistente());
				row.createCell(POS_STATO_SCOMPARSO, CellType.STRING).setCellValue(r.getStato_scomparso());
				row.createCell(POS_NOTE_SCOMPARSO, CellType.STRING).setCellValue(r.getNote_scomparso());
				row.createCell(POS_ALTRE_SCOMPARSO, CellType.STRING).setCellValue(r.getAltre_scomparso());
				row.createCell(POS_NECESSITA_RIPRISTINO, CellType.STRING).setCellValue(r.getNecessita_ripristino());
				row.createCell(POS_DESCRIZIONE_RIPRISTINO, CellType.STRING).setCellValue(r.getDescrizione_ripristino());
				row.createCell(POS_LATITUDE_RIPRISTINO, CellType.NUMERIC).setCellValue(r.getLatitude_ripristino());
				row.createCell(POS_LONGITUDE_RIPRISTINO, CellType.NUMERIC).setCellValue(r.getLongitude_ripristino());
				row.createCell(POS_ALTITUDE_RIPRISTINO, CellType.NUMERIC).setCellValue(r.getAltitude_ripristino());
				row.createCell(POS_ACCURACY_RIPRISTINO, CellType.NUMERIC).setCellValue(r.getAccuracy_ripristino());

				/*hyperlinkCell(row, workbook, hlink_style, POS_IMAGEMANUFATTO, r.getFoto_manufatto(), "http://www.angelobabini.it/calcifer/capisaldo/"+r.getFoto_manufatto());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEPANORAMICA, r.getFoto_panoramica(), "http://www.angelobabini.it/calcifer/capisaldo/"+r.getFoto_panoramica());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEAGGIORNATA, r.getFoto_aggiornata(), "http://www.angelobabini.it/calcifer/capisaldo/"+r.getFoto_aggiornata());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEDANNO_CONTRASSEGNO, r.getFoto_danno_contrassegno(), "http://www.angelobabini.it/calcifer/capisaldo/"+r.getFoto_danno_contrassegno());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEDANNO_MANUFATTO, r.getFoto_danno_manufatto(), "http://www.angelobabini.it/calcifer/capisaldo/"+r.getFoto_danno_manufatto());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGESITO_RIPRISTINO, r.getFoto_sito_ripristino(), "http://www.angelobabini.it/calcifer/capisaldo/"+r.getFoto_sito_ripristino());*/

				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEMANUFATTO, (r.getFoto_manufatto()!=null && r.getFoto_manufatto().length()>0 ? baseUrl : "") + r.getFoto_manufatto(), baseUrl + r.getFoto_manufatto());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEPANORAMICA, (r.getFoto_panoramica()!=null && r.getFoto_panoramica().length()>0 ? baseUrl : "") + r.getFoto_panoramica(), baseUrl + r.getFoto_panoramica());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEAGGIORNATA, (r.getFoto_aggiornata()!=null && r.getFoto_aggiornata().length()>0 ? baseUrl : "") + r.getFoto_aggiornata(), baseUrl + r.getFoto_aggiornata());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEDANNO_CONTRASSEGNO, (r.getFoto_danno_contrassegno()!=null && r.getFoto_danno_contrassegno().length()>0 ? baseUrl : "") + r.getFoto_danno_contrassegno(), baseUrl + r.getFoto_danno_contrassegno());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEDANNO_MANUFATTO, (r.getFoto_danno_manufatto()!=null && r.getFoto_danno_manufatto().length()>0 ? baseUrl : "") + r.getFoto_danno_manufatto(), baseUrl + r.getFoto_danno_manufatto());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGESITO_RIPRISTINO, (r.getFoto_sito_ripristino()!=null && r.getFoto_sito_ripristino().length()>0 ? baseUrl : "") + r.getFoto_sito_ripristino(), baseUrl + r.getFoto_sito_ripristino());
			}

			FileOutputStream out = new FileOutputStream(fileXLSX);
			workbook.write(out);
			out.close();

		} catch(Exception e) {
			throw e;
		} finally {
			/*try {
				_XSSFWorkbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static synchronized void exportXLSX(File fileXLSX, List<Ricognizione> ricognizioni, String baseUrl, boolean urlInLink) throws Exception {
		SXSSFWorkbook workbook = null;
		XSSFWorkbook _XSSFWorkbook = null;
		try {
			_XSSFWorkbook = new XSSFWorkbook();
			workbook = new SXSSFWorkbook(_XSSFWorkbook, 2);
			SXSSFSheet sheet = workbook.createSheet(fileXLSX.getName().replaceAll(".xlsx", ""));
			
			DataFormat fmt = workbook.createDataFormat();
			CellStyle text_style = workbook.createCellStyle();
			text_style.setDataFormat(fmt.getFormat("@"));
			sheet.setDefaultColumnStyle(3, text_style);
			
			CellStyle hlink_style = workbook.createCellStyle();
	        Font hlink_font = workbook.createFont();
	        hlink_font.setUnderline(Font.U_SINGLE);
	        hlink_font.setColor(IndexedColors.BLUE.getIndex());
	        hlink_style.setFont(hlink_font);
	        
	        CellStyle date_style = workbook.createCellStyle();
			date_style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("m/d/yy h:mm"));

			SXSSFRow row = sheet.createRow(0);
			row.createCell(POS_INIZIO, CellType.STRING).setCellValue("Inizio");
			row.createCell(POS_FINE, CellType.STRING).setCellValue("Fine");
			row.createCell(POS_OPERATORE, CellType.STRING).setCellValue("Operatore");
			row.createCell(POS_ID, CellType.STRING).setCellValue("Id");
			row.createCell(POS_ID_IN_ALTRE_RETI, CellType.STRING).setCellValue("Id_In_Altre_Reti");
			row.createCell(POS_ID_CAPOSALDO_PRINCIPALE, CellType.STRING).setCellValue("Id_Caposaldo_Principale");
			row.createCell(POS_LATITUDE, CellType.STRING).setCellValue("Latitude");
			row.createCell(POS_LONGITUDE, CellType.STRING).setCellValue("Longitude");
			row.createCell(POS_ALTITUDE, CellType.STRING).setCellValue("Altitude");
			row.createCell(POS_ACCURACY, CellType.STRING).setCellValue("Accuracy");
			row.createCell(POS_UBICAZIONE, CellType.STRING).setCellValue("Ubicazione");
			row.createCell(POS_INDIRIZZO, CellType.STRING).setCellValue("Indirizzo");
			row.createCell(POS_ACCESSO, CellType.STRING).setCellValue("Accesso");
			row.createCell(POS_POSIZIONE_CONTRASSEGNO, CellType.STRING).setCellValue("Posizione_Contrassegno");
			row.createCell(POS_MATERIALIZZAZIONE, CellType.STRING).setCellValue("Materializzazione");
			row.createCell(POS_CONTRASSEGNO_ANCORATO, CellType.STRING).setCellValue("Contrassegno_Ancorato");
			row.createCell(POS_CONTRASSEGNO_DANNEGGIATO, CellType.STRING).setCellValue("Contrassegno_Danneggiato");
			row.createCell(POS_DESCRIZIONE_DANNEGGIAMENTO, CellType.STRING).setCellValue("Descrizione_Danneggiamento");
			row.createCell(POS_TIPOLOGIA_FONDAZIONE, CellType.STRING).setCellValue("Tipologia_Fondazione");
			row.createCell(POS_ANOMALIE_MANUFATTO, CellType.STRING).setCellValue("Anomalie_Manufatto");
			row.createCell(POS_DESCRIZIONE_ANOMALIE, CellType.STRING).setCellValue("Descrizione_Anomalie");
			row.createCell(POS_NOTE_RILEVANTI, CellType.STRING).setCellValue("Note_Rilevanti");
			row.createCell(POS_DESCR_NOTE_RILEVANTI, CellType.STRING).setCellValue("Descr_Note_Rilevanti");
			row.createCell(POS_ALTRE_NOTE_RILEVANTI, CellType.STRING).setCellValue("Altre_Note_Rilevanti");
			row.createCell(POS_TIPO_CONTESTO_AMBIENTALE, CellType.STRING).setCellValue("Tipo_Contesto_Ambientale");
			row.createCell(POS_ALTRO_TIPO_CONTESTO_AMBIENTALE, CellType.STRING).setCellValue("Altro_Tipo_Contesto_Ambientale");
			row.createCell(POS_INSTANCEID, CellType.STRING).setCellValue("Instanceid");
			row.createCell(POS_MODIFICA, CellType.STRING).setCellValue("Modifica");
			row.createCell(POS_APPARTENENZA, CellType.STRING).setCellValue("Appartenenza");
			row.createCell(POS_AFFIDABILITA, CellType.STRING).setCellValue("Affidabilita");
			row.createCell(POS_ESISTENTE, CellType.STRING).setCellValue("Esistente");
			row.createCell(POS_STATO_SCOMPARSO, CellType.STRING).setCellValue("Stato_Scomparso");
			row.createCell(POS_NOTE_SCOMPARSO, CellType.STRING).setCellValue("Note_Scomparso");
			row.createCell(POS_ALTRE_SCOMPARSO, CellType.STRING).setCellValue("Altre_Scomparso");
			row.createCell(POS_NECESSITA_RIPRISTINO, CellType.STRING).setCellValue("Necessita_Ripristino");
			row.createCell(POS_DESCRIZIONE_RIPRISTINO, CellType.STRING).setCellValue("Descrizione_Ripristino");
			row.createCell(POS_LATITUDE_RIPRISTINO, CellType.STRING).setCellValue("Latitude_Ripristino");
			row.createCell(POS_LONGITUDE_RIPRISTINO, CellType.STRING).setCellValue("Longitude_Ripristino");
			row.createCell(POS_ALTITUDE_RIPRISTINO, CellType.STRING).setCellValue("Altitude_Ripristino");
			row.createCell(POS_ACCURACY_RIPRISTINO, CellType.STRING).setCellValue("Accuracy_Ripristino");
			row.createCell(POS_IMAGEMANUFATTO, CellType.STRING).setCellValue("Image Manufatto");
			row.createCell(POS_IMAGEPANORAMICA, CellType.STRING).setCellValue("Image Panoramica");
			row.createCell(POS_IMAGEAGGIORNATA, CellType.STRING).setCellValue("Image Aggiornata");
			row.createCell(POS_IMAGEDANNO_CONTRASSEGNO, CellType.STRING).setCellValue("imageDanno_Contrassegno");
			row.createCell(POS_IMAGEDANNO_MANUFATTO, CellType.STRING).setCellValue("imageDanno_Manufatto");
			row.createCell(POS_IMAGESITO_RIPRISTINO, CellType.STRING).setCellValue("imageSito_Ripristino");

			int i = 1;
			for(Ricognizione r : ricognizioni) {
				row = sheet.createRow(i);
				i++;
				dateCell(row, workbook, date_style, POS_INIZIO, r.getInizio());
				dateCell(row, workbook, date_style, POS_FINE, r.getFine());
				row.createCell(POS_OPERATORE, CellType.STRING).setCellValue(r.getOperatore());
				row.createCell(POS_ID, CellType.STRING).setCellValue(r.getId());
				row.getCell(POS_ID).setCellType(CellType.STRING);
				row.getCell(POS_ID).setCellStyle(text_style);
				row.createCell(POS_ID_IN_ALTRE_RETI, CellType.STRING).setCellValue(r.getId_in_altre_reti());
				row.getCell(POS_ID_IN_ALTRE_RETI).setCellType(CellType.STRING);
				row.getCell(POS_ID_IN_ALTRE_RETI).setCellStyle(text_style);
				row.createCell(POS_ID_CAPOSALDO_PRINCIPALE, CellType.STRING).setCellValue(r.getId_caposaldo_principale());
				row.getCell(POS_ID_CAPOSALDO_PRINCIPALE).setCellType(CellType.STRING);
				row.getCell(POS_ID_CAPOSALDO_PRINCIPALE).setCellStyle(text_style);
				row.createCell(POS_LATITUDE, CellType.NUMERIC).setCellValue(r.getLatitude());
				row.createCell(POS_LONGITUDE, CellType.NUMERIC).setCellValue(r.getLongitude());
				row.createCell(POS_ALTITUDE, CellType.NUMERIC).setCellValue(r.getAltitude());
				row.createCell(POS_ACCURACY, CellType.NUMERIC).setCellValue(r.getAccuracy());
				row.createCell(POS_UBICAZIONE, CellType.STRING).setCellValue(r.getUbicazione());
				row.createCell(POS_INDIRIZZO, CellType.STRING).setCellValue(r.getIndirizzo());
				row.createCell(POS_ACCESSO, CellType.STRING).setCellValue(r.getAccesso());
				row.createCell(POS_POSIZIONE_CONTRASSEGNO, CellType.STRING).setCellValue(r.getPosizione_contrassegno());
				row.createCell(POS_MATERIALIZZAZIONE, CellType.STRING).setCellValue(r.getMaterializzazione());
				row.createCell(POS_CONTRASSEGNO_ANCORATO, CellType.STRING).setCellValue(r.getContrassegno_ancorato());
				row.createCell(POS_CONTRASSEGNO_DANNEGGIATO, CellType.STRING).setCellValue(r.getContrassegno_danneggiato());
				row.createCell(POS_DESCRIZIONE_DANNEGGIAMENTO, CellType.STRING).setCellValue(r.getDescrizione_danneggiamento());
				row.createCell(POS_TIPOLOGIA_FONDAZIONE, CellType.STRING).setCellValue(r.getTipologia_fondazione());
				row.createCell(POS_ANOMALIE_MANUFATTO, CellType.STRING).setCellValue(r.getAnomalie_manufatto());
				row.createCell(POS_DESCRIZIONE_ANOMALIE, CellType.STRING).setCellValue(r.getDescrizione_anomalie());
				row.createCell(POS_NOTE_RILEVANTI, CellType.STRING).setCellValue(r.getNote_rilevanti());
				row.createCell(POS_DESCR_NOTE_RILEVANTI, CellType.STRING).setCellValue(r.getDescr_note_rilevanti());
				row.createCell(POS_ALTRE_NOTE_RILEVANTI, CellType.STRING).setCellValue(r.getAltre_note_rilevanti());
				row.createCell(POS_TIPO_CONTESTO_AMBIENTALE, CellType.STRING).setCellValue(r.getTipo_contesto_ambientale());
				row.createCell(POS_ALTRO_TIPO_CONTESTO_AMBIENTALE, CellType.STRING).setCellValue(r.getAltro_tipo_contesto_ambientale());
				row.createCell(POS_INSTANCEID, CellType.STRING).setCellValue(r.getInstanceID());
				dateCell(row, workbook, date_style, POS_MODIFICA, r.getModifica());
				row.createCell(POS_APPARTENENZA, CellType.STRING).setCellValue(r.getAppartenenza());
				row.createCell(POS_AFFIDABILITA, CellType.NUMERIC).setCellValue(r.getAffidabilita());
				row.createCell(POS_ESISTENTE, CellType.STRING).setCellValue(r.getEsistente());
				row.createCell(POS_STATO_SCOMPARSO, CellType.STRING).setCellValue(r.getStato_scomparso());
				row.createCell(POS_NOTE_SCOMPARSO, CellType.STRING).setCellValue(r.getNote_scomparso());
				row.createCell(POS_ALTRE_SCOMPARSO, CellType.STRING).setCellValue(r.getAltre_scomparso());
				row.createCell(POS_NECESSITA_RIPRISTINO, CellType.STRING).setCellValue(r.getNecessita_ripristino());
				row.createCell(POS_DESCRIZIONE_RIPRISTINO, CellType.STRING).setCellValue(r.getDescrizione_ripristino());
				row.createCell(POS_LATITUDE_RIPRISTINO, CellType.NUMERIC).setCellValue(r.getLatitude_ripristino());
				row.createCell(POS_LONGITUDE_RIPRISTINO, CellType.NUMERIC).setCellValue(r.getLongitude_ripristino());
				row.createCell(POS_ALTITUDE_RIPRISTINO, CellType.NUMERIC).setCellValue(r.getAltitude_ripristino());
				row.createCell(POS_ACCURACY_RIPRISTINO, CellType.NUMERIC).setCellValue(r.getAccuracy_ripristino());

				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEMANUFATTO, (urlInLink && r.getFoto_manufatto()!=null && r.getFoto_manufatto().length()>0 ? baseUrl : "") + r.getFoto_manufatto(), baseUrl + r.getFoto_manufatto());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEPANORAMICA, (urlInLink && r.getFoto_panoramica()!=null && r.getFoto_panoramica().length()>0 ? baseUrl : "") + r.getFoto_panoramica(), baseUrl + r.getFoto_panoramica());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEAGGIORNATA, (urlInLink && r.getFoto_aggiornata()!=null && r.getFoto_aggiornata().length()>0 ? baseUrl : "") + r.getFoto_aggiornata(), baseUrl + r.getFoto_aggiornata());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEDANNO_CONTRASSEGNO, (urlInLink && r.getFoto_danno_contrassegno()!=null && r.getFoto_danno_contrassegno().length()>0 ? baseUrl : "") + r.getFoto_danno_contrassegno(), baseUrl + r.getFoto_danno_contrassegno());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGEDANNO_MANUFATTO, (urlInLink && r.getFoto_danno_manufatto()!=null && r.getFoto_danno_manufatto().length()>0 ? baseUrl : "") + r.getFoto_danno_manufatto(), baseUrl + r.getFoto_danno_manufatto());
				hyperlinkCell(row, workbook, hlink_style, POS_IMAGESITO_RIPRISTINO, (urlInLink && r.getFoto_sito_ripristino()!=null && r.getFoto_sito_ripristino().length()>0 ? baseUrl : "") + r.getFoto_sito_ripristino(), baseUrl + r.getFoto_sito_ripristino());
			}

			FileOutputStream out = new FileOutputStream(fileXLSX);
			workbook.write(out);
			out.close();

		} catch(Exception e) {
			throw e;
		} finally {
			/*try {
				_XSSFWorkbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Cell dateCell(SXSSFRow row, SXSSFWorkbook workbook, CellStyle style, int pos, Date date) {
		if(date != null) {
			Cell cell = row.createCell(pos, CellType.NUMERIC);
			cell.setCellValue(date);
	        cell.setCellStyle(style);
	        return cell;
		} else {
			return null;
		}
	}
	
	private static Cell hyperlinkCell(SXSSFRow row, SXSSFWorkbook workbook, CellStyle style, int pos, String text, String url) {
		if(text != null && text.length() > 0) {
			Cell cell = row.createCell(pos, CellType.STRING);
			cell.setCellValue(text);
			Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
	        link.setAddress(url);
	        cell.setHyperlink(link);
	        cell.setCellStyle(style);
	        return cell;
		} else {
			return null;
		}
	}
}
