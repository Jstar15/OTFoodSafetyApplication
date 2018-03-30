package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import commons.IngrMenuObject;

public class QueryByDepartment {
	Connection conn = null;
	String query = null;
	Object[][] xmlobject = null;

	public QueryByDepartment(Connection conn) {
		this.conn = conn;
	}

	public void QueryNutritionMenu(int menuoption) {

		try {
			if (menuoption == 1) {
				query = "select Phrase_ID,Category_ID,Phrase_Desc from [SI_Translations].[dbo].[si_phrases] where Country_Code='Eng' and Category_ID='1021'"; // MAN
			} else {
				query = "select Phrase_ID,Category_ID,Phrase_Desc from [SI_Translations].[dbo].[si_phrases] where Country_Code='Eng' and Category_ID='1020'"; // BAKEHOUSE
			}

			// create the java statements
			Statement st = (Statement) conn.createStatement();
			ResultSet rs = (ResultSet) st.executeQuery(query);

			ArrayList<IngrMenuObject> tempobj = new ArrayList<IngrMenuObject>();

			// iterate through the java resultset
			while (rs.next()) {
				String phaseid = rs.getString("Phrase_ID");
				String catid = rs.getString("Category_ID");
				String desc = rs.getString("Phrase_Desc");

				tempobj.add(new IngrMenuObject(phaseid, catid, desc));
			}

			xmlobject = new Object[tempobj.size()][3];

			for (int j = 0; j < tempobj.size(); j++) {
				xmlobject[j][0] = tempobj.get(j).getPhaseid();
				xmlobject[j][1] = tempobj.get(j).getCatid();
				xmlobject[j][2] = tempobj.get(j).getDesc();
			}

			st.close();

		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}

	public Object[][] getXmlobject() {
		return xmlobject;
	}

}
