/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dabomstew.pkrandom.gui;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;

import com.dabomstew.pkrandom.FileFunctions;

/**
 * 
 * @author Stewart
 */
public class UpdateFoundDialog extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1641494584047215623L;
	private int targetVersion;

	/**
	 * Creates new form UpdateFoundDialog
	 */
	public UpdateFoundDialog(RandomizerGUI parent, int newVersion,
			String changelog) {
		super(parent, true);
		initComponents();
		targetVersion = newVersion;
		Scanner sc = new Scanner(changelog);
		String firstLine = htmlspecialchars(sc.nextLine());
		sc.close();
		updateFoundLabel
				.setText("<html>An update to the Randomizer has been found: <b>"
						+ firstLine
						+ ".</b><br />You can see the changes made since your last update below.");
		while (changelog.trim().toLowerCase().startsWith("<html>")) {
			changelog = changelog.trim().substring(6);
		}
		updateChangelogArea.setText(changelog);
		updateChangelogArea.setCaretPosition(0);
		this.setLocationRelativeTo(parent);
		this.setVisible(true);

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		updateFoundLabel = new javax.swing.JLabel();
		clScroller = new javax.swing.JScrollPane();
		updateChangelogArea = new javax.swing.JTextArea();
		downloadUpdateBtn = new javax.swing.JButton();
		closeBtn = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		updateFoundLabel
				.setText("<html>An update to the Randomizer has been found: <b>x.x.x</b>, released <b>blah.</b><br /> You can see the changes made below.");

		clScroller
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		updateChangelogArea.setEditable(false);
		updateChangelogArea.setColumns(40);
		updateChangelogArea.setLineWrap(true);
		updateChangelogArea.setRows(5);
		updateChangelogArea.setWrapStyleWord(true);
		updateChangelogArea.setEnabled(false);
		clScroller.setViewportView(updateChangelogArea);

		downloadUpdateBtn.setText("Download Update Now");
		downloadUpdateBtn
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						downloadUpdateBtnActionPerformed(evt);
					}
				});

		closeBtn.setText("Close Without Updating");
		closeBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeBtnActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(clScroller)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		updateFoundLabel,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(0,
																		161,
																		Short.MAX_VALUE))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		downloadUpdateBtn)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(
																		closeBtn)))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(updateFoundLabel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(clScroller,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										209,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(downloadUpdateBtn)
												.addComponent(closeBtn))
								.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void downloadUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_downloadUpdateBtnActionPerformed
		// External: download delta file
		String deltaFile = "delta_" + RandomizerGUI.UPDATE_VERSION + "_"
				+ targetVersion + ".zip";
		try {
			byte[] zip = FileFunctions
					.downloadFile("http://pokehacks.dabomstew.com/randomizer/autoupdate/"
							+ deltaFile);
			extract(zip, new File("./"));
			JOptionPane
					.showMessageDialog(
							this,
							"Update complete - the randomizer will now close.\n"
									+ "You should now re-open the program and begin using the new version.");
			System.exit(0);
		} catch (IOException ex) {
			JOptionPane
					.showMessageDialog(
							this,
							"Automatic update not available.\n"
									+ "You will now be taken to the website to download it manually.");
			attemptOpenBrowser();
		}
	}// GEN-LAST:event_downloadUpdateBtnActionPerformed

	private void attemptOpenBrowser() {
		String targetURL = "http://pokehacks.dabomstew.com/randomizer/downloads.php";
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
				: null;

		if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE)) {
			JOptionPane
					.showMessageDialog(
							this,
							"The downloads page could not be opened automatically.\n"
									+ "Open a browser and navigate to the below link to download the update:\n"
									+ targetURL);
		} else {
			try {
				desktop.browse(new URI(targetURL));
			} catch (Exception e) {
				JOptionPane
						.showMessageDialog(
								this,
								"The downloads page could not be opened automatically.\n"
										+ "Open a browser and navigate to the below link to download the update:\n"
										+ targetURL);
			}
		}
		this.setVisible(false);
	}

	private void extractFile(ZipInputStream in, File outdir, String name)
			throws IOException {
		byte[] buffer = new byte[4096];
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(new File(outdir, name)));
		int count = -1;
		while ((count = in.read(buffer)) != -1)
			out.write(buffer, 0, count);
		out.close();
	}

	private void mkdirs(File outdir, String path) {
		File d = new File(outdir, path);
		if (!d.exists())
			d.mkdirs();
	}

	private String dirpart(String name) {
		int s = name.lastIndexOf(File.separatorChar);
		return s == -1 ? null : name.substring(0, s);
	}

	private String htmlspecialchars(String original) {
		return original.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;");
	}

	public void extract(byte[] zipfile, File outdir) throws IOException {
		ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(
				zipfile));
		ZipEntry entry;
		String name, dir;
		while ((entry = zin.getNextEntry()) != null) {
			name = entry.getName();
			if (entry.isDirectory()) {
				mkdirs(outdir, name);
				continue;
			}
			/*
			 * this part is necessary because file entry can come before
			 * directory entry where is file located i.e.: /foo/foo.txt /foo/
			 */
			dir = dirpart(name);
			if (dir != null)
				mkdirs(outdir, dir);

			extractFile(zin, outdir, name);
		}
		zin.close();
	}

	private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_closeBtnActionPerformed
		this.setVisible(false);
	}// GEN-LAST:event_closeBtnActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JScrollPane clScroller;
	private javax.swing.JButton closeBtn;
	private javax.swing.JButton downloadUpdateBtn;
	private javax.swing.JTextArea updateChangelogArea;
	private javax.swing.JLabel updateFoundLabel;
	// End of variables declaration//GEN-END:variables
}
