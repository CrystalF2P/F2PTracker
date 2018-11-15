import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class F2PTrackerGUI extends JFrame {

	JButton addButton, searchButton;
	JTextPane textPane;
	// File cl = new File(System.getProperty("user.dir") +
	// "\\characterList.txt");
	File cl = new File("CharList");
	File op = new File("Rankingoutput");
	Scanner sc;
	String name, realm, region, accountType, apiCall = "", inputLine, points, kills, total, level, f2pATotal = "",
			vetATotal = "", hkTotal = "", temp;
	String intro = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/><title>F2P & Vet Rankings</title></head><body><h1>F2P & Vet Rankings</h1>\n\n";
	String outro = "<br><br>Questions: Message me (Crystalpall) on XPOFF</body></html>";
	int i;
	BufferedReader in;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					F2PTrackerGUI frame = new F2PTrackerGUI();
					frame.setVisible(true);
				} catch (Exception e) {

				}
			}
		});
	}

	public F2PTrackerGUI() throws FileNotFoundException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 650, 800);
		setTitle("F2P Tracker");
		getContentPane().setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JTextPane textPane = new JTextPane();
		textPane.setText("");
		// textPane.setEditable(false);
		textPane.setContentType("text/html");
		scrollPane.setViewportView(textPane);

		JLabel lblTest = new JLabel(" ");
		getContentPane().add(lblTest, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JButton addButton = new JButton("Add Character");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		JButton searchButton = new JButton("     Search     ");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sc = new Scanner(cl);
					f2pATotal = "";
					vetATotal = "";
					hkTotal = "";

					while (sc.hasNext()) {
						apiCall = "";
						name = sc.nextLine();
						realm = sc.nextLine();
						region = sc.nextLine();
						accountType = sc.nextLine();

						// System.out.println(region);

						try {
							temp = "https://" + region + ".api.battle.net/wow/character/" + realm + "/" + name
									+ "?fields=achievements&locale=en_" + region
									+ "&apikey=anrdwxky5vpxtcqfkfuajybzk687qzd9";
							System.out.println(temp);
							// // make API call, update to your correct one
							// URL oracle = new URL(temp);
							// in = new BufferedReader(new
							// InputStreamReader(oracle.openStream()));
							// while ((inputLine = in.readLine()) != null)
							// apiCall += inputLine;

							String apiCall = new Scanner(new URL(temp).openStream(), "UTF-8").useDelimiter("\\A")
									.next();

							// URLConnection conn = oracle.openConnection();
							// try (BufferedReader reader = new BufferedReader(
							// new InputStreamReader(conn.getInputStream(),
							// StandardCharsets.UTF_8))) {
							// apiCall =
							// reader.lines().collect(Collectors.joining("\n"));
							// }

							//System.out.println(apiCall);
							// jump 50 ahead to get past the name
							// otherwise will cause issues if person named level
							// 400 should be enough to clear
							level = apiCall.substring(50, 400);
							level = level.substring(level.indexOf("level") + 7);
							level = level.substring(0, level.indexOf(","));

							// System.out.println(level);
							// level 20 check
							if (Integer.parseInt(level) == 20) {

								// get to achi points and then jump 50 ahead
								points = apiCall.substring(apiCall.indexOf("achievementPoints") + 19,
										apiCall.indexOf("achievementPoints") + 50);
								points = points.substring(0, points.indexOf(","));

								// its about 7 characters from the end that
								// kills are saved, 50 back should be fine
								kills = apiCall.substring(apiCall.length() - 50, apiCall.length());
								kills = kills.substring(kills.indexOf("totalHonorableKills") + 21, kills.length() - 1);

								if (accountType.equals("f2p")) {
									f2pATotal += points + "\t" + "<a href=\"https://worldofwarcraft.com/en-" + region + "/character/" + realm + "/" + name +"\">" + name + "</a>" + "\t" + region.toUpperCase() + "\n";

								} else if (accountType.equals("vet")) {
									vetATotal += points + "\t" + "<a href=\"https://worldofwarcraft.com/en-" + region + "/character/" + realm + "/" + name +"\">" + name + "</a>" + "\t"  + region.toUpperCase() + "\n";
								}
								hkTotal += kills + "\t" + "<a href=\"https://worldofwarcraft.com/en-" + region + "/character/" + realm + "/" + name +"\">" + name + "</a>" + "\t" + region.toUpperCase() + "\n";

							}
						} catch (Exception exc) {
							// System.out.println(exc);
						}
					}
					// sort the results
					if (f2pATotal.length() > 0)
						f2pATotal = sort(f2pATotal);
					if (vetATotal.length() > 0)
						vetATotal = sort(vetATotal);
					if (hkTotal.length() > 0)
						hkTotal = sort(hkTotal);

				try{

					BufferedWriter writer = new BufferedWriter(new FileWriter(op));
					writer.write(intro);
				
					

					total = "<html><font face=arial><b style=\"color:blue\"><font size=5>F2P ACHIEVEMENT RANKING</b style><font size=3>";
					sc = new Scanner(f2pATotal);
					while (sc.hasNext()) {
						total += "<br><html>" + sc.nextLine();
					}

					total += "<br><br><br><html><font face=arial><b style=\"color:blue\"><font size=5>VET ACHIEVEMENT RANKING</b style><font size=3>";
					sc = new Scanner(vetATotal);
					while (sc.hasNext()) {
						total += "<br><html>" + sc.nextLine();
					}
					total += "<br><br><br><html><b style=\"color:red\"><font size=5>HONORABLE KILLS</b style><font size=3>";
					sc = new Scanner(hkTotal);
					while (sc.hasNext()) {
						total += "<br><html>" + sc.nextLine();
					}

					textPane.setText(total);

					writer.write(total);
					writer.write(outro);
					writer.close();
				}
				catch(IOException e2) {
					//e2.printStackTrace();
				}
				} catch (FileNotFoundException e1) {
					// System.out.println(e1);
				}

			}
		});
		panel.add(searchButton);

	}

	public String sort(String s) {
		String[] allParts = s.split("\n");
		ArrayList<String> array = new ArrayList<String>();
		String elementToAdd;
		s = "";
		int positionToRemove;

		for (int i = 0; i < allParts.length; i++) {
			array.add(allParts[i]);
		}

		while (!array.isEmpty()) {
			elementToAdd = array.get(0).trim();
			positionToRemove = 0;
			for (int i = 0; i < array.size(); i++) {
				// if the number at the front of the string is greater then the
				// number of the front of the string at the first element, set
				// the element to add to the element you found
				if (Integer.parseInt(array.get(i).substring(0, array.get(i).indexOf("\t"))) > Integer
						.parseInt(elementToAdd.substring(0, elementToAdd.indexOf("\t")))) {
					elementToAdd = array.get(i);
					positionToRemove = i;
				}
			}
			s += elementToAdd + "\n";
			array.remove(positionToRemove);
		}
		return s.trim();
	}

}
