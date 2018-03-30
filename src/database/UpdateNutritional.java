package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class UpdateNutritional {
	Connection conn = null;
	String query = null;
	Object[][] xmlobject = null;
	Boolean success = false;

	public UpdateNutritional(Connection conn) {
		this.conn = conn;
	}

	public void UpdateNutritionalToDatabase(String pcode, String ingredients, String desc) {

		try {
			// create our java prepared statement using a sql update query
			if (pcode.trim().length() > 0) {

				String s = "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;" + "BEGIN TRANSACTION;"
						+ " UPDATE [SI_Translations].[dbo].[si_phrases] SET Phrase=?, phrase_html = ? WHERE Category_ID=? AND  Phrase_ID = ?"
						+ " IF @@ROWCOUNT = 0" + " BEGIN"
						+ " INSERT INTO [SI_Translations].[dbo].[si_phrases] (Phrase,  phrase_html, Category_ID, Phrase_ID, active, Country_Code, Phrase_Desc) VALUES (?,?,?,?,?,?,?) "
						+ " END" + " COMMIT TRANSACTION;";

				PreparedStatement ps = conn.prepareStatement(s);

				// set the prepared statement parameters
				ps.setString(1, ingredients);
				ps.setString(2, ingredients);
				ps.setString(3, "1029");
				ps.setString(4, pcode);

				ps.setString(5, ingredients);
				ps.setString(6, ingredients);
				ps.setString(7, "1029");
				ps.setString(8, pcode);
				ps.setString(9, "1");
				ps.setString(10, "Eng");
				ps.setString(11, "PackagingSpec For " + pcode + " (" + desc + ")");

				// call executeUpdate to execute our sql update statement
				ps.executeUpdate();
				ps.close();

				success = true;
			} else {
				JOptionPane.showMessageDialog(null, "Error 23: Updating Nutritional Data Failed. Invalid key");
				// p.s. this should never happen // just in case

			}
		} catch (SQLException e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			JOptionPane.showMessageDialog(null, "Error 24: Updating Nutritional Data Failed. SQL Exception.");
		}
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Object[][] getXmlobject() {
		return xmlobject;
	}

}
