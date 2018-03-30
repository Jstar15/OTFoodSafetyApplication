package backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import commons.IngrMenuObject;
import commons.ProductIngredientsObject;
import database.ConnectionClass;
import database.QueryAllData;

//this class creates an excel file containing a full backup of all ingredients data
public class ExcelCreateIngredientFile {
	private String path = "";

	@SuppressWarnings("deprecation")
	public ExcelCreateIngredientFile() {
		// get template
		InputStream fis;
		HSSFWorkbook wb = null;
		try {
			fis = new FileInputStream(new File("BackupTemplate.xls"));
			wb = new HSSFWorkbook(fis);
			fis.close();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// query database and get all dat require for backup
		ConnectionClass conn = new ConnectionClass();
		QueryAllData q = new QueryAllData(conn.getConn());
		try {
			q.QueryAll();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// setup all fonts and cell styles
		Font font = wb.createFont();// Create font
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);// Make font bold

		HSSFCellStyle cellStyleheader = wb.createCellStyle();
		cellStyleheader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleheader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyleheader.setFont(font);// set it to bold
		cellStyleheader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyleheader.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyleheader.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyleheader.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		HSSFCellStyle cellStyleingr = wb.createCellStyle();
		cellStyleingr.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		cellStyleingr.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyleingr.setWrapText(true);// set it to bold
		cellStyleingr.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyleingr.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyleingr.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyleingr.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		CellStyle style = wb.createCellStyle();// Create style
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		// loop through array and populate excel workbook
		ArrayList<IngrMenuObject> arr = q.getQueryArray();
		for (int x = 0; x < arr.size(); x++) {
			wb.cloneSheet(0);
			wb.setSheetName(x + 2, arr.get(x).getPhaseid() + " - " + arr.get(x).getCatid());

			// add desciption to template
			HSSFSheet firstSheet = wb.getSheetAt(x + 2);

			HSSFRow rowA = firstSheet.createRow(0);

			HSSFCell cellA = rowA.createCell(0);
			cellA.setCellStyle(cellStyleheader);
			cellA.setCellValue(new HSSFRichTextString("Ingredients:"));

			HSSFCell cellA0 = rowA.createCell(1);
			cellA0.setCellStyle(cellStyleheader);
			cellA0.setCellValue(new HSSFRichTextString(arr.get(x).getDesc()));

			HSSFCell cellA1 = rowA.createCell(2);
			cellA1.setCellStyle(cellStyleheader);
			cellA1.setCellValue(new HSSFRichTextString(arr.get(x).getPhaseid()));

			HSSFCell cellA2 = rowA.createCell(3);
			cellA2.setCellStyle(cellStyleheader);
			cellA2.setCellValue(new HSSFRichTextString(arr.get(x).getCatid()));

			HSSFCell backbtncell = rowA.createCell(4);
			backbtncell.setCellStyle(cellStyleheader);
			backbtncell.setCellValue(new HSSFRichTextString("Back To Main Menu"));

			HSSFRow rowA2 = firstSheet.createRow(1);
			HSSFCell cellingr = rowA2.createCell(0);
			cellingr.setCellStyle(cellStyleingr);
			cellingr.setCellValue(new HSSFRichTextString(arr.get(x).getIngr().trim().replaceAll("\\<[^>]*>", "")
					.replaceAll("\\r", "").replaceAll("\\n", "")));

			// for each pcode update to sheet
			int pcoderow = 4;
			for (ProductIngredientsObject pio : arr.get(x).getPioarray()) {

				HSSFRow rowA3 = firstSheet.createRow(pcoderow);

				HSSFCell cellA4 = rowA3.createCell(0);
				cellA4.setCellStyle(style);
				cellA4.setCellValue(new HSSFRichTextString(pio.getDescription()));

				HSSFCell cellA3 = rowA3.createCell(1);
				cellA3.setCellStyle(style);
				cellA3.setCellValue(new HSSFRichTextString(pio.getPcode()));

				HSSFCell cellA5 = rowA3.createCell(2);
				cellA5.setCellStyle(style);
				cellA5.setCellValue(new HSSFRichTextString(pio.getLabelname()));

				HSSFCell cellA6 = rowA3.createCell(3);
				cellA6.setCellStyle(style);
				cellA6.setCellValue(new HSSFRichTextString(pio.getMeatpercentile()));

				// HSSFCell cellA7 = rowA3.createCell(4);
				// cellA7.setCellStyle(style);
				// cellA7.setCellValue(new HSSFRichTextString(pio.getPackaginginfo()));

				pcoderow = pcoderow + 1;
			}
		}

		wb.getCreationHelper().createFormulaEvaluator().evaluateAll();

		File f = new File("IngredientsInfo.xls");
		path = f.getAbsolutePath();
		try (FileOutputStream fos = new FileOutputStream(f)) {
			wb.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return path;
	}

}
