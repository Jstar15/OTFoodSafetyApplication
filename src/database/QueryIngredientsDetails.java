
package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import commons.ProductIngredientsObject;

public class QueryIngredientsDetails {
	private Connection conn = null;
	private String query = null;
	private Object[][] xmlobject = null;
	private ArrayList<ProductIngredientsObject> tempobj;

	public void QueryIngredientsData(String catid, String phaseid) {
		String wherestr = "";

		if (catid.equals("1021")) {
			wherestr = "Pcpart03='" + phaseid + "'";
		} else if (catid.equals("1020")) {
			wherestr = "Pcpart01='" + phaseid + "'";
		}

		query = "select product, description, PLULabelName, lifespan, Pcpart02 from [SI].[dbo].[inventory] where activeflag='Y' AND "
				+ wherestr;

		// create the java statements
		Statement st;
		ResultSet rs;
		tempobj = new ArrayList<ProductIngredientsObject>();
		ProductIngredientsObject p = null;
		// query selected group products
		try {
			st = (Statement) conn.createStatement();
			rs = (ResultSet) st.executeQuery(query);

			while (rs.next()) {
				String pcode = rs.getString("product");
				String desc = rs.getString("description");
				String label = rs.getString("PLULabelName");
				String lifespan = rs.getString("lifespan");
				String storage = rs.getString("Pcpart02");

				p = new ProductIngredientsObject();
				p.setPcode(pcode);
				p.setDescription(desc);
				p.setLabelname(label);
				p.setLifespan(lifespan);
				p.setStorage(storage);
				tempobj.add(p);
			}

			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// query storage from PC part 2

		// query and get on info
		for (ProductIngredientsObject o : tempobj) {
			String basecode = o.getPcode();
			basecode = basecode.replace("/FZ", "");
			basecode = basecode.replace("/TR", "");
			basecode = basecode.replace("/CR", "");
			basecode = basecode.replace("/WIP", "");
			basecode = basecode.trim();

			try {
				// query lifespan fz //split up into multiple querys
				query = "select lifespan from [SI].[dbo].[inventory] where activeflag='Y' AND (Pcpart01!=99 or Pcpart03!=99) AND product LIKE '"
						+ basecode + "/FZ'";
				st = (Statement) conn.createStatement();
				rs = (ResultSet) st.executeQuery(query);
				while (rs.next()) {
					String lifespanfz = rs.getString("lifespan");
					o.setLifespanfz(lifespanfz);
				}
				st.close();

				// query lifespan tr
				query = "select lifespan from [SI].[dbo].[inventory] where activeflag='Y' AND (Pcpart01!=99 or Pcpart03!=99) AND product LIKE '"
						+ basecode + "/TR'";
				st = (Statement) conn.createStatement();
				rs = (ResultSet) st.executeQuery(query);
				while (rs.next()) {
					String lifespantr = rs.getString("lifespan");
					o.setLifespantr(lifespantr);
				}
				st.close();

				// query lifespan cr
				query = "select lifespan from [SI].[dbo].[inventory] where activeflag='Y' AND (Pcpart01!=99 or Pcpart03!=99) AND product LIKE '"
						+ basecode + "/CR'";

				st = (Statement) conn.createStatement();
				rs = (ResultSet) st.executeQuery(query);
				while (rs.next()) {
					String lifespancr = rs.getString("lifespan");
					o.setLifespancr(lifespancr);
				}
				st.close();

				// query percentile
				query = "select Phrase_html from [SI_Translations].[dbo].[si_phrases] where Phrase_ID='" + o.getPcode()
						+ "'" + " and Category_ID='1023'";

				st = (Statement) conn.createStatement();
				rs = (ResultSet) st.executeQuery(query);
				while (rs.next()) {
					String percentile = rs.getString("Phrase_html");
					o.setMeatpercentile(percentile);
				}
				st.close();

				// query nutritional data
				query = "select Phrase_html from [SI_Translations].[dbo].[si_phrases] where Phrase_ID='" + o.getPcode()
						+ "'" + " and Category_ID='1029'";
				st = (Statement) conn.createStatement();
				rs = (ResultSet) st.executeQuery(query);
				while (rs.next()) {
					String nutri = rs.getString("Phrase_html");
					o.setNutrition(nutri);
				}
				st.close();

				// query storage info
				query = "select Phrase_html from [SI_Translations].[dbo].[si_phrases] where Phrase_ID='"
						+ o.getStorage() + "'" + " and Category_ID='22'";

				st = (Statement) conn.createStatement();
				rs = (ResultSet) st.executeQuery(query);
				while (rs.next()) {
					String storage = rs.getString("Phrase_html");
					o.setStorage(storage);
				}
				st.close();/////////////////////////////////////////////////////////////////////////////////////////////////////////// ad
							/////////////////////////////////////////////////////////////////////////////////////////////////////////// eNG
							/////////////////////////////////////////////////////////////////////////////////////////////////////////// to
							/////////////////////////////////////////////////////////////////////////////////////////////////////////// querys

				// query storage info
				query = "select Phrase_html from [SI_Translations].[dbo].[si_phrases] where Phrase_ID='" + o.getPcode()
						+ "'" + " AND Category_ID='1028' AND [Country_Code]='Eng'";

				st = (Statement) conn.createStatement();
				rs = (ResultSet) st.executeQuery(query);
				while (rs.next()) {
					String packinfo = rs.getString("Phrase_html");
					o.setPackaginginfo(packinfo);
				}
				st.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// query ingredients info
		try {
			query = "select Phrase_html from [SI_Translations].[dbo].[si_phrases] where Country_Code='Eng' and Category_ID='"
					+ catid + "' and Phrase_ID='" + phaseid + "'";

			st = (Statement) conn.createStatement();
			rs = (ResultSet) st.executeQuery(query);
			while (rs.next()) {
				String ingri = rs.getString("Phrase_html");
				for (ProductIngredientsObject o : tempobj) {
					o.setIngredients(ingri);
				}
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Object[][] getXmlobject() {
		xmlobject = new Object[tempobj.size()][4];

		for (int j = 0; j < tempobj.size(); j++) {
			xmlobject[j][0] = tempobj.get(j).getPcode();
			xmlobject[j][1] = tempobj.get(j).getDescription();
			xmlobject[j][2] = tempobj.get(j).getLabelname();
			xmlobject[j][3] = tempobj.get(j).getMeatpercentile();
		}
		return xmlobject;
	}

	public ArrayList<ProductIngredientsObject> getTempobj() {
		return tempobj;
	}

	public void setTempobj(ArrayList<ProductIngredientsObject> tempobj) {
		this.tempobj = tempobj;
	}

	public QueryIngredientsDetails(Connection conn) {
		this.conn = conn;
	}

}
