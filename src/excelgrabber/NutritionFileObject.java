package excelgrabber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import XML.XMLReader;

public class NutritionFileObject {
	ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
	private HSSFWorkbook wb;

	// cycle through sheets in nutitioanl database excel file and extract all
	// data field to an array
	public NutritionFileObject() {

		InputStream ExcelFileToRead;
		try {
			XMLReader x = new XMLReader();
			String URL = x.getNutri();
			ExcelFileToRead = new FileInputStream(URL);
			wb = new HSSFWorkbook(ExcelFileToRead);

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i); // Get Your Sheet.
				for (Row row : sheet) { // For each Row.

					for (int y = 12; y <= 43; y++) {

						String pcode = row.getCell(y) + "";
						if (pcode.trim().equals("")) {
							// do nothing
						} else {
							ArrayList<String> result = new ArrayList<String>();
							result.add(pcode + ""); // pcode
							result.add(row.getCell(0) + ""); // product
							result.add(FormatCellAsString3(row.getCell(2), "KJ"));// kj
							result.add(FormatCellAsString3(row.getCell(1), "Kcal"));// kcal
							result.add(FormatCellAsString2(row.getCell(6), "g"));// fat
							result.add(FormatCellAsString2(row.getCell(7), "g"));// saturates
							result.add(FormatCellAsString2(row.getCell(4), "g"));// Carbohydrate
							result.add(FormatCellAsString2(row.getCell(5), "g"));// sugars
							result.add(FormatCellAsString2(row.getCell(8), "g"));// fibre
							result.add(FormatCellAsString2(row.getCell(3), "g"));// protein
							result.add(FormatCellAsString(row.getCell(9), "g"));// sodium

							// result.add(FormatCellAsString2(row.getCell(10), "g"));// salt is sodium * 2.5

							if (!pcode.equals("null")) {
								results.add(result);
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			ArrayList<String> result = new ArrayList<String>();
			result.add("");
			result.add("");
			result.add("");
			result.add("");
			result.add("");
			result.add("");
			result.add("");
			result.add("");
			result.add("");
			result.add("");
			result.add("");
			results.add(result);
		}
	}

	// port to search the results array //turn input to base code
	public ArrayList<String> SearchNutri(String searchcode) {
		searchcode = searchcode.replaceAll("/FZ", "");
		searchcode = searchcode.replaceAll("/CR", "");
		searchcode = searchcode.replaceAll("/TR", "");
		searchcode = searchcode.replaceAll("/WIP", "");
		searchcode = searchcode.replaceAll("/MA", "");
		searchcode = searchcode.trim();

		for (int x = 0; x < results.size(); x++) {
			if (results.get(x).get(0).trim().toUpperCase().equals(searchcode.toUpperCase())) {
				return results.get(x);
			}
		}
		return null;
	}

	// 2 decimal places multiplied by 2.5
	public String FormatCellAsString(Cell c, String end) {
		try {
			DataFormatter formatter2 = new DataFormatter();
			String val = formatter2.formatCellValue(c);
			float f = Float.parseFloat(val);
			f = (float) (f * 2.5);
			String s = String.format("%.02f", f);
			return s + end;
		} catch (Exception e) {
			return end;// salt
		}
	}

	// two decimal places
	public String FormatCellAsString2(Cell c, String end) {
		try {
			DataFormatter formatter2 = new DataFormatter();
			String val = formatter2.formatCellValue(c);
			float f = Float.parseFloat(val);
			String s = String.format("%.01f", f);
			return s + end;
		} catch (Exception e) {
			return end;// salt
		}
	}

	// no decimal places
	public String FormatCellAsString3(Cell c, String end) {
		try {
			DataFormatter formatter2 = new DataFormatter();
			String val = formatter2.formatCellValue(c);
			float f = Float.parseFloat(val);
			String s = String.format("%.00f", f);
			return s + end;
		} catch (Exception e) {
			return end;// salt
		}
	}

}
