package backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import XML.XMLReader;

public class BackupDataToZip {

	public BackupDataToZip() {

		// Create new jframe component
		JFrame parentFrame = new JFrame();

		// get save location for backup
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Specify a file to save");

		String zipFile = "";
		int userSelection = fileChooser.showSaveDialog(parentFrame);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			zipFile = fileToSave.getAbsolutePath() + ".zip";

			// get file location from xml file
			XMLReader x = new XMLReader();
			ArrayList<String> s = new ArrayList<String>();

			if (CheckFileExists(x.getAller())) {
				s.add(x.getAller());
			}

			if (CheckFileExists(x.getNutri())) {
				s.add(x.getNutri());
			}

			if (CheckFileExists(x.getPackspec())) {
				s.add(x.getPackspec());
			}

			ExcelCreateIngredientFile e = new ExcelCreateIngredientFile();
			if (CheckFileExists(e.getFileName())) {
				s.add(e.getFileName());
			}

			// save backup to file
			try {
				// create byte buffer
				byte[] buffer = new byte[100024];

				FileOutputStream fos = new FileOutputStream(zipFile);
				ZipOutputStream zos = new ZipOutputStream(fos);

				for (int i = 0; i < s.size(); i++) {

					File srcFile = new File(s.get(i));
					FileInputStream fis = new FileInputStream(srcFile);

					// begin writing a new ZIP entry, positions the stream to
					// the start of the entry data
					zos.putNextEntry(new ZipEntry(srcFile.getName()));

					int length;
					while ((length = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, length);
					}

					zos.closeEntry();
					fis.close();
				}

				// close the ZipOutputStream
				zos.close();
				JOptionPane.showMessageDialog(null, "Backup has been successful.\n " + s.size() + " of 4");

			} catch (IOException ioe) {
				System.out.println("Error creating zip file: " + ioe);
				JOptionPane.showMessageDialog(null, "Backup error occured. " + ioe.getMessage());
			}
		}
	}

	private Boolean CheckFileExists(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

}