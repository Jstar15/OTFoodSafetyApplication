package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class UpdateIngredients {
	Connection conn = null;
	String query = null;
	Object[][] xmlobject = null;
	Boolean success = false;

	public UpdateIngredients(Connection conn) {
		this.conn = conn;
	}

	public void UpdateIngredientsToDatabase(String catid, String phraseid, String html, String rtf) {

		try {
			// create our java prepared statement using a sql update query
			if (catid.trim().length() > 0 && phraseid.trim().length() > 0) {
				PreparedStatement ps = conn.prepareStatement(
						"UPDATE [SI_Translations].[dbo].[si_phrases] SET Phrase=?, phrase_html = ? WHERE Category_ID=? AND  Phrase_ID = ?");

				// set the prepared statement parameters
				ps.setString(1, rtf);
				ps.setString(2, html);
				ps.setString(3, catid);
				ps.setString(4, phraseid);

				// call executeUpdate to execute our sql update statement
				ps.executeUpdate();
				ps.close();

				success = true;
			} else {
				JOptionPane.showMessageDialog(null, "Error 22: updating Ingredients failed. Invalid key");
				// p.s. this should never happen // just in case

			}
		} catch (SQLException e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
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
