package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import commons.IngrMenuObject;

public class QueryAllData {
	private Connection conn = null;
	private String query = null;
	private ArrayList<IngrMenuObject> queryarray;

	public QueryAllData(Connection conn) {
		this.conn = conn;
	}

	public ArrayList<IngrMenuObject> getQueryArray() {
		return queryarray;
	}

	public void setQueryArray(ArrayList<IngrMenuObject> queryarray) {
		this.queryarray = queryarray;
	}

	public void QueryAll() throws SQLException {
		query = "select Phrase_ID,Category_ID,Phrase_Desc, phrase_html from [SI_Translations].[dbo].[si_phrases] where Country_Code='Eng' and ( Category_ID='1021' or Category_ID='1020' )"; // MAN

		Statement st = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) st.executeQuery(query);

		queryarray = new ArrayList<IngrMenuObject>();

		// iterate through the java resultset
		while (rs.next()) {
			String phaseid = rs.getString("Phrase_ID");
			String catid = rs.getString("Category_ID");
			String desc = rs.getString("Phrase_Desc");
			String ingr = rs.getString("phrase_html");

			// query all products were phase id and catid matches
			ConnectionClass conn = new ConnectionClass();
			QueryIngredientsDetails q = new QueryIngredientsDetails(conn.getConn());
			q.QueryIngredientsData(catid, phaseid);

			queryarray.add(new IngrMenuObject(phaseid, catid, desc, ingr, q.getTempobj()));
		}

		st.close();

	}

}
