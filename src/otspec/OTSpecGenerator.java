package otspec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import XML.XMLReader;
import commons.ProductIngredientsObject;

public class OTSpecGenerator {
	private HWPFDocument doc;
	private String filePath = "template.doc";
	private String tmpPath = "tmp.doc";
	private String destfilePath = "";

	public String getDestfilePath() {
		return destfilePath;
	}

	public OTSpecGenerator(ProductIngredientsObject p, ArrayList<String> allerarray, String specnum) {
		POIFSFileSystem fs = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(filePath));
			doc = new HWPFDocument(fs);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// load files from databse object
		String basecode = p.getPcode();
		basecode = basecode.replace("/FZ", "");
		basecode = basecode.replace("/TR", "");
		basecode = basecode.replace("/CR", "");
		basecode = basecode.replace("/WIP", "");

		doc = replaceText(doc, "@@PCODE", basecode);
		doc = replaceText(doc, "@@PRODUCT", p.getDescription());
		doc = replaceText(doc, "@@CRYO", p.getLifespancr());
		doc = replaceText(doc, "@@FRESH", p.getLifespantr());
		doc = replaceText(doc, "@@FROZEN", p.getLifespanfz());

		if (p.getMeatpercentile().trim().length() == 0) {

			doc = replaceText(doc, "@@INGR", "INGREDIENTS: " + CleanHtml(p.getIngredients()) + ";");
		} else {
			doc = replaceText(doc, "@@INGR",
					"INGREDIENTS: " + CleanHtml(p.getMeatpercentile()) + "; " + CleanHtml(p.getIngredients()) + ";");
		}

		doc = replaceText(doc, "@@ADDDEC", getallergensdeclarations(p.getIngredients()));

		doc = replaceText(doc, "@@PACKAGING", p.getPackaginginfo());

		try {
			String[] arr = p.getStorage().split("<br />");
			doc = replaceText(doc, "@@STORE1", CleanHtmlBasic(arr[0]));
			doc = replaceText(doc, "@@STORE2", CleanHtmlBasic(arr[1]));
		} catch (Exception e) {
			doc = replaceText(doc, "@@STORE1", "");
			doc = replaceText(doc, "@@STORE2", "");
		}

		doc = replaceText(doc, "@@ADDDEC", getallergensdeclarations(p.getIngredients()));

		doc = replaceText(doc, "@@GMO", getGmostatement(p.getIngredients()));

		doc = replaceText(doc, "@@m2", ConvertAnswerOppo(getMsg(p.getIngredients()), 1));
		doc = replaceText(doc, "@@m1", ConvertAnswerOppo(getMsg(p.getIngredients()), 2));

		doc = replaceText(doc, "@@c1", ConvertAnswer(getCollic(p.getIngredients()), 1));
		doc = replaceText(doc, "@@c2", ConvertAnswer(getCollic(p.getIngredients()), 2));

		doc = replaceText(doc, "@@ve1", ConvertAnswer(getVEGI(p.getIngredients()), 1));
		doc = replaceText(doc, "@@ve2", ConvertAnswer(getVEGI(p.getIngredients()), 2));

		doc = replaceText(doc, "@@vi1", ConvertAnswer(getVEGAN(p.getIngredients()), 1));
		doc = replaceText(doc, "@@vi2", ConvertAnswer(getVEGAN(p.getIngredients()), 2));

		doc = replaceText(doc, "@@SPEC", specnum);

		try {
			// load alergens info from excel object
			doc = replaceText(doc, "@@a10", ConvertAnswer(allerarray.get(10), 1));
			doc = replaceText(doc, "@@a11", ConvertAnswer(allerarray.get(13), 1));
			doc = replaceText(doc, "@@a12", ConvertAnswer(allerarray.get(15), 1));
			doc = replaceText(doc, "@@a13", ConvertAnswer(allerarray.get(7), 1));
			doc = replaceText(doc, "@@a14", ConvertAnswer(allerarray.get(9), 1));
			doc = replaceText(doc, "@@a1", ConvertAnswer(allerarray.get(3), 1));
			doc = replaceText(doc, "@@a2", ConvertAnswer(allerarray.get(5), 1));
			doc = replaceText(doc, "@@a3", ConvertAnswer(allerarray.get(6), 1));
			doc = replaceText(doc, "@@a4", ConvertAnswer(allerarray.get(4), 1));
			doc = replaceText(doc, "@@a5", ConvertAnswer(allerarray.get(6), 1));
			doc = replaceText(doc, "@@a6", ConvertAnswer(allerarray.get(14), 1));
			doc = replaceText(doc, "@@a7", ConvertAnswer(allerarray.get(8), 1));
			doc = replaceText(doc, "@@a8", ConvertAnswer(allerarray.get(11), 1));
			doc = replaceText(doc, "@@a9", ConvertAnswer(allerarray.get(2), 1));

			doc = replaceText(doc, "@@b10", ConvertAnswer(allerarray.get(10), 2));
			doc = replaceText(doc, "@@b11", ConvertAnswer(allerarray.get(13), 2));
			doc = replaceText(doc, "@@b12", ConvertAnswer(allerarray.get(15), 2));
			doc = replaceText(doc, "@@b13", ConvertAnswer(allerarray.get(7), 2));
			doc = replaceText(doc, "@@b14", ConvertAnswer(allerarray.get(5), 2));
			doc = replaceText(doc, "@@b1", ConvertAnswer(allerarray.get(3), 2));
			doc = replaceText(doc, "@@b2", ConvertAnswer(allerarray.get(5), 2));
			doc = replaceText(doc, "@@b3", ConvertAnswer(allerarray.get(6), 2));
			doc = replaceText(doc, "@@b4", ConvertAnswer(allerarray.get(4), 2));
			doc = replaceText(doc, "@@b5", ConvertAnswer(allerarray.get(12), 2));
			doc = replaceText(doc, "@@b6", ConvertAnswer(allerarray.get(14), 2));
			doc = replaceText(doc, "@@b7", ConvertAnswer(allerarray.get(8), 2));
			doc = replaceText(doc, "@@b8", ConvertAnswer(allerarray.get(11), 2));
			doc = replaceText(doc, "@@b9", ConvertAnswer(allerarray.get(2), 2));

		} catch (Exception e) {
			e.printStackTrace();
			// empty all all fields that could not be found
			JOptionPane.showMessageDialog(null, "Error Loading Data | Allergens Info Could Not Be Found");
			doc = replaceText(doc, "@@a10", "");
			doc = replaceText(doc, "@@a11", "");
			doc = replaceText(doc, "@@a12", "");
			doc = replaceText(doc, "@@a13", "");
			doc = replaceText(doc, "@@a14", "");
			doc = replaceText(doc, "@@a1", "");
			doc = replaceText(doc, "@@a2", "");
			doc = replaceText(doc, "@@a3", "");
			doc = replaceText(doc, "@@a4", "");
			doc = replaceText(doc, "@@a5", "");
			doc = replaceText(doc, "@@a6", "");
			doc = replaceText(doc, "@@a7", "");
			doc = replaceText(doc, "@@a8", "");
			doc = replaceText(doc, "@@a9", "");
			doc = replaceText(doc, "@@b10", "");
			doc = replaceText(doc, "@@b11", "");
			doc = replaceText(doc, "@@b12", "");
			doc = replaceText(doc, "@@b13", "");
			doc = replaceText(doc, "@@b14", "");
			doc = replaceText(doc, "@@b1", "");
			doc = replaceText(doc, "@@b2", "");
			doc = replaceText(doc, "@@b3", "");
			doc = replaceText(doc, "@@b4", "");
			doc = replaceText(doc, "@@b5", "");
			doc = replaceText(doc, "@@b6", "");
			doc = replaceText(doc, "@@b7", "");
			doc = replaceText(doc, "@@b8", "");
			doc = replaceText(doc, "@@b9", "");
			doc = replaceText(doc, "@@SPEC", "");
		}

		// String nutrition = "10g/<br />5kj<br />5g<br />5g<br />5g<br />5g<br
		// />5g<br />5g<br />5g";
		// load nutritional data
		try {

			String[] split = p.getNutritionexcel().split("<br />");
			// String[] split = nutrition.split("<br />");
			doc = replaceText(doc, "@@N1", split[0]);
			doc = replaceText(doc, "@@N2", split[1]);
			doc = replaceText(doc, "@@N3", split[2]);
			doc = replaceText(doc, "@@N4", split[3]);
			doc = replaceText(doc, "@@N5", split[4]);
			doc = replaceText(doc, "@@N6", split[5]);
			doc = replaceText(doc, "@@N7", split[6]);
			doc = replaceText(doc, "@@N8", split[7]);
			doc = replaceText(doc, "@@N9", split[8]);

		} catch (Exception e) {// ignore
			JOptionPane.showMessageDialog(null, "Error Loading Data | Nutritional Info Could Not Be Found");
			doc = replaceText(doc, "@@N1", "");
			doc = replaceText(doc, "@@N2", "");
			doc = replaceText(doc, "@@N3", "");
			doc = replaceText(doc, "@@N4", "");
			doc = replaceText(doc, "@@N5", "");
			doc = replaceText(doc, "@@N6", "");
			doc = replaceText(doc, "@@N7", "");
			doc = replaceText(doc, "@@N8", "");
			doc = replaceText(doc, "@@N9", "");
		}
	}

	public void SaveToFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(null);
		destfilePath = chooser.getSelectedFile().toPath().toString();

		// ensure correct extension for saving
		if (!("      " + destfilePath.substring(destfilePath.length() - 4)).toLowerCase().equals(".doc")) {
			destfilePath = destfilePath + ".doc";

			try {
				saveWord(destfilePath, doc);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error Saving File | Check File is not already open!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getallergensdeclarations(String input) {
		String output = "";
		Pattern pattern = Pattern.compile("[A-Z0-9a-z ()-]+[(| ]?E([0-9][0-9]?([0-9]|[a-z])+([0-9]|[a-z)]))");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			String tmp = matcher.group().trim();
			output = output + tmp + ", ";
		}

		return output;
	}

	private String getCollic(String input) {
		String output = "Y";
		XMLReader x = new XMLReader();
		String reg = x.getColicacsblacklist().replace("@@split", "|").toLowerCase();
		reg = reg.substring(0, reg.length() - 1);
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(input.toLowerCase());
		while (matcher.find()) {
			output = "";
			;
		}

		return output;
	}

	private String getVEGI(String input) {
		String output = "Y";
		XMLReader x = new XMLReader();
		String reg = x.getVegeterianblacklist().replace("@@split", "|").toLowerCase();
		reg = reg.substring(0, reg.length() - 1);
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(input.toLowerCase());
		while (matcher.find()) {
			output = "";
			;
		}

		return output;
	}

	private String getVEGAN(String input) {
		String output = "Y";
		XMLReader x = new XMLReader();
		String reg = x.getVeganblacklist().replace("@@split", "|").toLowerCase();
		reg = reg.substring(0, reg.length() - 1);
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(input.toLowerCase());
		while (matcher.find()) {
			output = "";
		}

		if (output.equals("Y")) {
			output = getVEGI(input);
		}

		return output;
	}

	private String getMsg(String input) {
		String output = "Y";
		XMLReader x = new XMLReader();
		String reg = x.getMsgblacklist().replace("@@split", "|").toLowerCase();
		reg = reg.substring(0, reg.length() - 1);
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(input.toLowerCase());
		while (matcher.find()) {
			output = "";
		}

		return output;
	}

	private String getGmostatement(String input) {
		XMLReader x = new XMLReader();
		String output = "This product does not contain any genetically modified ingredients.";
		String reg = x.getGmoblacklist().replace("@@split", "|").toLowerCase();
		reg = reg.substring(0, reg.length() - 1);
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			output = "This product does contain genetically modified ingredients.";

		}
		return output;
	}

	private String ConvertAnswer(String s, int mode) {
		if (s.equals("Y") && mode == 1) {
			return "YES";
		} else if (s.equals("Y") && mode == 2) {
			return "";
		} else if (mode == 2) {
			return "NO";
		} else {
			return "";
		}
	}

	private String ConvertAnswerOppo(String s, int mode) {
		if (s.equals("Y") && mode == 1) {
			return "NO";
		} else if (s.equals("Y") && mode == 2) {
			return "";
		} else if (mode == 2) {
			return "YES";
		} else {
			return "";
		}
	}

	private static HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText) {
		Range r1 = doc.getRange();
		for (int i = 0; i < r1.numSections(); ++i) {
			Section s = r1.getSection(i);
			for (int x = 0; x < s.numParagraphs(); x++) {
				Paragraph p = s.getParagraph(x);
				for (int z = 0; z < p.numCharacterRuns(); z++) {
					CharacterRun run = p.getCharacterRun(z);
					String text = run.text();
					if (text.contains(findText)) {
						run.replaceText(findText, replaceText);
					}
				}
			}
		}
		return doc;
	}

	private static void saveWord(String filePath, HWPFDocument doc) throws FileNotFoundException, IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			doc.write(out);
		} finally {
			out.close();
		}
	}

	public File getFileTemp() {
		try {
			saveWord(tmpPath, doc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new File(tmpPath);
	}

	// format html for saving output
	private String CleanHtml(String html) {

		html = html.replaceAll("</strong>", "");
		html = html.replaceAll("<strong>", "");

		Whitelist wl = Whitelist.simpleText();
		html = Jsoup.clean(html, wl);

		html = html.replaceAll("&nbsp", "");
		html = html.replaceAll("<u>", "");
		html = html.replaceAll("<b>", "");
		html = html.replaceAll("<i>", "");

		html = html.replaceAll("</u>", "");
		html = html.replaceAll("</b>", "");
		html = html.replaceAll("</i>", "");

		html = html.replaceAll("\\r", "");
		html = html.replaceAll("\\n", "");

		html = html.replaceAll(",,,", ",");
		html = html.replaceAll(",,", ",");

		html = html.trim();
		System.out.println(html);
		System.out.println(html.replaceAll("\\<[^>]*>", ""));
		return html.replaceAll("\\<[^>]*>", "");
	}

	private String CleanHtmlBasic(String s) {
		s = s.replaceAll("<B>", "").replaceAll("</B>", "");
		s = s.replaceAll("<U>", "").replaceAll("</U>", "");
		s = s.replaceAll("<I>", "").replaceAll("</I>", "");
		return s;
	}
}