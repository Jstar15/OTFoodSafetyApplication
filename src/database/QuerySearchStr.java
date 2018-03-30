package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import commons.IngrMenuObject;

public class QuerySearchStr {
	private Connection conn = null;
	private String query = null;
	private Object[][] xmlobject = null;

	public QuerySearchStr(Connection conn) {
		this.conn = conn;
	}

	public void QueryNutritionMenu(String searchstr, int option) throws SQLException {
		String qtype = "";

		if (option == 1) {
			qtype = "product LIKE '";
		} else if (option == 2) {
			qtype = "description LIKE '%";
		}

		query = "select product, description, PLULabelName,Pcpart01,Pcpart03 from [SI_TS_GI1].[dbo].[inventory] where activeflag='Y' AND ((Pcpart01!=99 AND Pcpart03=99) OR (Pcpart01=99 AND Pcpart03!=99))  AND "
				+ qtype + searchstr + "%'"; // MAN
		// create the java statements
		Statement st = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) st.executeQuery(query);

		ArrayList<IngrMenuObject> tempobj = new ArrayList<IngrMenuObject>();

		// iterate through the java resultset
		while (rs.next()) {
			String pcpart1 = rs.getString("Pcpart01");
			String pcpart3 = rs.getString("Pcpart03");
			String desc = rs.getString("description");
			String pcode = rs.getString("product");
			String catid = "";
			String phaseid = "";

			if (!pcpart1.equals("99")) {
				catid = "1020";
				phaseid = pcpart1;
			} else if (!pcpart3.equals("99")) {
				catid = "1021";
				phaseid = pcpart3;
			} else {
				catid = "5050505050505";
				phaseid = "5050505050505";
			}
			tempobj.add(new IngrMenuObject(phaseid, catid, desc, pcode));
		}

		xmlobject = new Object[tempobj.size()][4];

		for (int j = 0; j < tempobj.size(); j++) {
			xmlobject[j][3] = tempobj.get(j).getPhaseid();
			xmlobject[j][2] = tempobj.get(j).getCatid();
			xmlobject[j][1] = tempobj.get(j).getDesc();
			xmlobject[j][0] = tempobj.get(j).getPcode();

		}
		st.close();
	}

	public Object[][] getXmlobject() {
		return xmlobject;
	}

}
