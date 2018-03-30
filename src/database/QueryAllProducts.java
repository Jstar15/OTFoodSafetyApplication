package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import commons.IngrMenuObject;

public class QueryAllProducts {
	private Connection conn = null;
	private String query = null;
	private Object[][] xmlobject = null;
	private ArrayList<IngrMenuObject> queryarray;

	public QueryAllProducts(Connection conn) {
		this.conn = conn;
	}

	public ArrayList<IngrMenuObject> getQueryArray() {
		return queryarray;
	}

	public void setQueryArray(ArrayList<IngrMenuObject> queryarray) {
		this.queryarray = queryarray;
	}

	public void QueryAllCodes() throws SQLException {

		query = "select product, description, PLULabelName,Pcpart01,Pcpart03 from [SI_TS_GI1].[dbo].[inventory] where activeflag='Y' AND ((Pcpart01!=99 AND Pcpart03=99) OR (Pcpart01=99 AND Pcpart03!=99)) order by product asc"; // MAN

		// AND PLULabelName != 'pie.silt' deleted line

		// create the java statements
		Statement st = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) st.executeQuery(query);

		queryarray = new ArrayList<IngrMenuObject>();

		// iterate through the java resultset
		while (rs.next()) {
			String label = rs.getString("PLULabelName");
			String desc = rs.getString("description");
			String pcode = rs.getString("product");
			String phaseid = "";

			queryarray.add(new IngrMenuObject(phaseid, label, desc, pcode));
		}

		xmlobject = new Object[queryarray.size()][4];

		for (int j = 0; j < queryarray.size(); j++) {
			xmlobject[j][3] = queryarray.get(j).getPhaseid();
			xmlobject[j][2] = queryarray.get(j).getCatid();
			xmlobject[j][1] = queryarray.get(j).getDesc();
			xmlobject[j][0] = queryarray.get(j).getPcode();
		}
		st.close();

	}

	public Object[][] getXmlobject() {
		return xmlobject;
	}

}
