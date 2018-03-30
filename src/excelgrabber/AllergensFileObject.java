package excelgrabber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import XML.XMLReader;

public class AllergensFileObject {
	ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
	private HSSFWorkbook wb;

	// cycle through sheets in allergen database excel file and extract all data
	// field to an array
	public AllergensFileObject() {

		InputStream ExcelFileToRead;
		try {
			XMLReader x = new XMLReader();
			String URL = x.getAller();
			ExcelFileToRead = new FileInputStream(URL);
			wb = new HSSFWorkbook(ExcelFileToRead);

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i); // Get Your Sheet.
				for (Row row : sheet) { // For each Row.
					ArrayList<String> result = new ArrayList<String>();

					// nutri info
					result.add(row.getCell(1) + ""); // pcode
					result.add(row.getCell(2) + ""); // product
					result.add(row.getCell(7) + "");
					result.add(row.getCell(8) + "");
					result.add(row.getCell(9) + "");
					result.add(row.getCell(10) + "");
					result.add(row.getCell(11) + "");
					result.add(row.getCell(12) + "");
					result.add(row.getCell(13) + "");
					result.add(row.getCell(14) + "");
					result.add(row.getCell(15) + "");
					result.add(row.getCell(16) + "");
					result.add(row.getCell(17) + "");
					result.add(row.getCell(18) + "");
					result.add(row.getCell(19) + "");
					result.add(row.getCell(20) + "");
					result.add(row.getCell(21) + "");
					result.add(row.getCell(22) + "");
					result.add(row.getCell(0) + "");

					results.add(result);
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
	public ArrayList<String> SearchAller(String searchcode) {
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
}
