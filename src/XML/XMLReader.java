package XML;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {
	private Object[][] xmlobject = null;
	private String location = "";
	private String nutri = "";
	private String aller = "";
	private String packspec = "";
	private String allergenblacklist;
	private String colicacsblacklist;
	private String vegeterianblacklist;
	private String veganblacklist;
	private String gmoblacklist;
	private String msgblacklist;

	public XMLReader() {
		try {
			File stocks = new File("devices.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();

			NodeList nodes = doc.getElementsByTagName("node");
			List<Object> list = new ArrayList<Object>();
			xmlobject = new Object[nodes.getLength()][6];
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					// create tmp list and generate table object
					list.add(getValue("location", element));
					list.add(getValue("nutrilocation", element));
					list.add(getValue("allergenslocation", element));
					list.add(getValue("allergenblacklist", element));
					list.add(getValue("colicacsblacklist", element));
					list.add(getValue("vegeterianblacklist", element));
					list.add(getValue("veganblacklist", element));
					list.add(getValue("gmoblacklist", element));
					list.add(getValue("packspeclocation", element));
					list.add(getValue("msgblacklist", element));
				}
				location = list.get(0).toString();
				nutri = list.get(1).toString();
				aller = list.get(2).toString();
				allergenblacklist = list.get(3).toString();
				colicacsblacklist = list.get(4).toString();
				vegeterianblacklist = list.get(5).toString();
				veganblacklist = list.get(6).toString();
				gmoblacklist = list.get(7).toString();
				packspec = list.get(8).toString();
				msgblacklist = list.get(9).toString();

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String[][] parseStringtoArray(String str) {
		String[][] array = null;
		try {
			String[] split = str.split("@@split");
			ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
			for (String s : split) {
				arrayList.add(new ArrayList<String>(Arrays.asList("" + s)));
			}

			// convert back to String[][] and update table
			array = new String[arrayList.size()][];
			for (int i = 0; i < arrayList.size(); i++) {
				ArrayList<String> row = arrayList.get(i);
				array[i] = row.toArray(new String[row.size()]);
			}

		} catch (Exception e) {

		}
		return array;
	}

	public ArrayList<String> getPackspec2() {
		String[] split = packspec.split("@@split");
		ArrayList<String> arrayList = new ArrayList<String>();
		for (String s : split) {
			arrayList.add("" + s);
		}
		return arrayList;
	}

	private static String getValue(String tag, Element element) {
		try {
			NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
			Node node = (Node) nodes.item(0);
			return node.getNodeValue();
		} catch (NullPointerException e) {
			return "";
		}
	}

	public String[][] getAllergenblacklistarray() {
		return parseStringtoArray(allergenblacklist);
	}

	public String[][] getColicacsblacklistarray() {
		return parseStringtoArray(colicacsblacklist);
	}

	public String[][] getVegeterianblacklistarray() {
		return parseStringtoArray(vegeterianblacklist);
	}

	public String[][] getVeganblacklistarray() {
		return parseStringtoArray(veganblacklist);
	}

	public String[][] getPackspecarray() {
		return parseStringtoArray(packspec);
	}

	public String[][] getGmoBlacklistarray() {
		return parseStringtoArray(gmoblacklist);
	}

	public String[][] getMsgblacklistarray() {
		return parseStringtoArray(msgblacklist);
	}

	public String getMsgblacklist() {
		return msgblacklist;
	}

	public String getColicacsblacklist() {
		return colicacsblacklist;
	}

	public String getGmoblacklist() {
		return gmoblacklist;
	}

	public String getVegeterianblacklist() {
		return vegeterianblacklist;
	}

	public String getVeganblacklist() {
		return veganblacklist;
	}

	public String getNutri() {
		return nutri;
	}

	public String getAller() {
		return aller;
	}

	public String getLocation() {
		return location;
	}

	// used to populate a table l8ter on
	public Object[][] getXmlobject() {
		return xmlobject;
	}

	public String getPackspec() {
		return packspec;
	}
}
