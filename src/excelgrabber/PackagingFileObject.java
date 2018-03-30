package excelgrabber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import XML.XMLReader;

public class PackagingFileObject {
	ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
	private HSSFWorkbook wb;

	// cycle through sheets in allergen database excel file and extract all data
	// field to an array
	public PackagingFileObject() {

		InputStream ExcelFileToRead;
		try {
			XMLReader x = new XMLReader();
			String URL = x.getPackspec();
			ExcelFileToRead = new FileInputStream(URL);
			wb = new HSSFWorkbook(ExcelFileToRead);

			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				Sheet sheet = wb.getSheetAt(i); // Get Your Sheet.
				String packtype = sheet.getRow(1).getCell(1).toString();
				for (Row row : sheet) { // For each Row.
					ArrayList<String> result = new ArrayList<String>();

					// nutri info
					result.add(row.getCell(3) + ""); // pcode
					result.add(packtype + ""); // pcode
					results.add(result);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			ArrayList<String> result = new ArrayList<String>();
			result.add("");
			results.add(result);
		}
	}

	// port to search the results array //turn input to base code
	public String SearchPack(String searchcode) {
		searchcode = searchcode.replaceAll("/FZ", "");
		searchcode = searchcode.replaceAll("/CR", "");
		searchcode = searchcode.replaceAll("/TR", "");
		searchcode = searchcode.replaceAll("/WIP", "");
		searchcode = searchcode.trim();
		String followon = "";
		StringBuilder s = new StringBuilder();
		for (int x = 0; x < results.size(); x++) {
			if (results.get(x).get(0).trim().toUpperCase().equals(searchcode.toUpperCase())) {
				s.append(followon + results.get(x).get(1));
				followon = ", ";
			}
		}
		return s.toString();
	}

	// port to search the results array //turn input to base code
	public ArrayList<String> isPack(String searchcode) {
		searchcode = searchcode.replaceAll("/FZ", "");
		searchcode = searchcode.replaceAll("/CR", "");
		searchcode = searchcode.replaceAll("/TR", "");
		searchcode = searchcode.replaceAll("/WIP", "");
		searchcode = searchcode.trim();
		for (int x = 0; x < results.size(); x++) {
			if (results.get(x).get(0).trim().toUpperCase().equals(searchcode.toUpperCase())) {
				return results.get(x);
			}
		}
		return null;
	}
}
