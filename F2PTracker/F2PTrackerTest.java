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
import java.util.Date;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class F2PTrackerTest extends JFrame {

	JButton addButton, searchButton;
	JTextPane textPane;
	// File cl = new File(System.getProperty("user.dir") +
	// "\\characterList.txt");
	File cl = new File("CharList");
	File op = new File("Rankingoutput");
	Scanner sc;
	int deaths;
	int enumeration = 1;
	String name, realm, region, accountType, apiCall = "", inputLine, points, kills, total, level, hardcore, f2pATotal = "", hkTotal = "", hcATotal = "", f2pAccWideTotal ="", temp, hcTemp, checkTemp, accCheck;


	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	Date terazdata = new Date();

	String intro = "[size=7][color=yellow]F2P & Vet Rankings[/color][/size]";
	String outro = "[size=1][color=red]Questions? Comment or PM me[/color][/size]";
	String achiPanel = "[size=5][color=#00ffff]Character Only Achievement Rankings [/color][/size]";
	String accwideAchiPanel = "[size=5][color=#286fe0]Account Wide Achievement Rankings [/color][/size]";
	String hkPanel = "[size=5][color=#59b300] Honorable Kills Rankings [/color][/size]";
	String hcPanel = "[size=5][color=red] Hardcore Achievement Rankings [/color][/size]";
	String listBegin = "[list]";
	String listEnd = "[/list]";

	String access_token = "USlDG3VDgJLBciqiHk6hr7fGrr4UlCZ5N6"; // ACCESS TOKEN, IMPORTANT, NEEDS TO BE REFRESHED AT BATTLE NET DEV PORTAL

	int i;
	BufferedReader in;

	private static String encodeValue(String value){
		try{
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					F2PTrackerTest frame = new F2PTrackerTest();
					frame.setVisible(true);
				} catch (Exception e) {

				}
			}
		});
	}

	public F2PTrackerTest() throws FileNotFoundException {
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
					f2pAccWideTotal = "";
					hkTotal = "";
					hcATotal = "";

					while (sc.hasNext()) {
						apiCall = "";
						name = sc.nextLine();
						realm = sc.nextLine();
						region = sc.nextLine();
						accountType = sc.nextLine();

						// System.out.println(region);

						try {

	String baseUrl = "https://" + region + ".api.blizzard.com/wow/character/" + realm + "/";
	String restUrl = "?fields=statistics&locale=en_" + region + "&access_token=" + access_token + "";
	String restCheckUrl = "?fields=achievements&locale=en_" + region + "&access_token=" + access_token + "";


							String encodedName = encodeValue(name);
							hcTemp = baseUrl + encodedName + restUrl;
							temp = baseUrl + encodedName + restUrl;
							checkTemp = baseUrl + encodedName + restCheckUrl;
							System.out.println(temp);
							
							


							temp = baseUrl+ encodedName + restUrl;
							//System.out.println(temp);

							String hcApiCall = new Scanner(new URL(hcTemp).openStream(), "UTF-8").useDelimiter("\\A")
									.next();

							hardcore = hcApiCall.substring(9500, 11500);

							//System.out.println(""+ hardcore +"");

							hardcore = hardcore.substring(hardcore.indexOf("Total deaths") + 25);
							hardcore = hardcore.substring(0, hardcore.indexOf(","));
							deaths = Integer.parseInt(hardcore);
							//System.out.println(""+ deaths +"");    

							String apiCall = new Scanner(new URL(temp).openStream(), "UTF-8").useDelimiter("\\A")
									.next();

							//System.out.println(apiCall);

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

							String accwideCall = new Scanner(new URL(checkTemp).openStream(), "UTF-8").useDelimiter("\\A")
									.next();
							
							accCheck = accwideCall.substring(50, 10000);
							boolean isAccWide = accCheck.contains("1017,");
							System.out.println(isAccWide);
				

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

								System.out.println(points);
								System.out.println(kills);

								if(isAccWide == false){
								f2pATotal += points + "\t" + "[url=https://worldofwarcraft.com/en-" + region + "/character/" + region + "/" + realm + "/" + name + "]" + name + "[/url]" + "\t" + "[b]" + region.toUpperCase() + " " + accountType.toUpperCase() + "[/b]\n";
}

								if(isAccWide == true){
								f2pAccWideTotal += points + "\t" + "[url=https://worldofwarcraft.com/en-" + region + "/character/" + region + "/" + realm + "/" + name + "]" + name + "[/url]" + "\t" + "[b]" + region.toUpperCase() + " " + accountType.toUpperCase() + "[/b]\n";
}
								
								hkTotal +=  kills  + "\t" + "[url=https://worldofwarcraft.com/en-" + region + "/character/" + region + "/" + realm + "/" + name + "]" + name + "[/url]" + "\t" + "[b]" + region.toUpperCase() + " " + accountType.toUpperCase() + "[/b]\n";

								if (deaths == 0){
									hcATotal += points + "\t" + "[url=https://worldofwarcraft.com/en-" + region + "/character/" + region + "/" + realm + "/" + name + "]" + name + "[/url]" + "\t" + "[b]" + region.toUpperCase() + " " + accountType.toUpperCase() + "[/b]\n";
								}
							}
						} catch (Exception exc) {
							// System.out.println(exc);
						}
					}

					// sort the results
					if (f2pATotal.length() > 0)
						f2pATotal = sort(f2pATotal);
					if (f2pAccWideTotal.length() > 0)
						f2pAccWideTotal = sort(f2pAccWideTotal);
					if (hkTotal.length() > 0)
						hkTotal = sort(hkTotal);
					if (hcATotal.length() > 0)
						hcATotal = sort(hcATotal);

				try{

					BufferedWriter writer = new BufferedWriter(new FileWriter(op));
					
					writer.write(intro);
					
					//----- char-only achievement rankings
				
					total = "";
					sc = new Scanner(f2pATotal);
					while (sc.hasNext()) {
						total += "[*]" +enumeration+ ". " + sc.nextLine();
						enumeration++;
					}
					
					enumeration = 1;
					
					writer.write(achiPanel);
					writer.write(listBegin);
					writer.write(total);
					writer.write(listEnd);

					//----- acc-wide achievement rankings

					total = "";
					sc = new Scanner(f2pAccWideTotal);
					while (sc.hasNext()) {
						total += "[*]" +enumeration+ ". " + sc.nextLine();
						enumeration++;
					}
					
					enumeration = 1;
					
					writer.write(accwideAchiPanel);
					writer.write(listBegin);
					writer.write(total);
					writer.write(listEnd);
					
					//----- hk rankings
					
					total = "";
					sc = new Scanner(hkTotal);
					while (sc.hasNext()) {
						total += "[*]" +enumeration+ ". " + sc.nextLine();
						enumeration++;
					}
					
					enumeration = 1;

					writer.write(hkPanel);
					writer.write(listBegin);
					writer.write(total);
					writer.write(listEnd);
					
					//----- hardcore achievement rankings
					
					
					total = "";
					sc = new Scanner(hcATotal);
					while (sc.hasNext()) {
						total += "[*]" +enumeration+ ". " + sc.nextLine();
						enumeration++;
					}
					
					enumeration = 1;
					
					writer.write(hcPanel);
					writer.write(listBegin);
					writer.write(total);
					writer.write(listEnd);
					
					
					textPane.setText("done");

					writer.write(outro);
					writer.write("Last updated - ");
					writer.write(dateFormat.format(terazdata));
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
