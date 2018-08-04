package io.github.glott.ColorToggle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Display
{
	private static JFrame frame;
	private JPanel panel;

	private JButton TCWButton;
	private JButton TDWButton;

	public Display()
	{
		setTDWColors();
		TCWButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setTDW(false);
			}
		});

		TDWButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setTDW(true);
			}
		});
	}

	private static String readFile(String path)
			throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, StandardCharsets.UTF_8);
	}

	public void run()
	{
		frame = new JFrame("ColorToggle");
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	private void setTDWColors()
	{
		String s = "";
		try
		{
			s = readFile(System.getProperty("user.home") + "\\AppData\\Roaming\\vSTARS\\vSTARSConfig.xml");
		} catch (Exception ignored)
		{
		}
		boolean tdw = s.contains("ColorVideoMapsA Red=\"153\" Green=\"99\" Blue=\"0\"");
		if (tdw)
		{
			TDWButton.setBackground(new Color(46, 204, 113));
			TCWButton.setBackground(new Color(227, 227, 227));
		} else
		{
			TDWButton.setBackground(new Color(227, 227, 227));
			TCWButton.setBackground(new Color(46, 204, 113));
		}
	}

	private void setTDW(boolean tdw)
	{
		try
		{
			File f = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\vSTARS\\vSTARSConfig.xml");
			String s = readFile(f.getPath());
			s = tdw ? s.replaceAll("ColorVideoMapsA.*", "ColorVideoMapsA Red=\"153\" Green=\"99\" Blue=\"0\" />") :
					s.replaceAll("ColorVideoMapsA.*", "ColorVideoMapsA Red=\"140\" Green=\"140\" Blue=\"140\" />");
			s = tdw ? s.replaceAll("ColorVideoMapsB.*", "ColorVideoMapsB Red=\"153\" Green=\"99\" Blue=\"0\" />") :
					s.replaceAll("ColorVideoMapsB.*", "ColorVideoMapsB Red=\"140\" Green=\"140\" Blue=\"140\" />");
			PrintWriter out = new PrintWriter(f.getPath());
			out.println(s);
			out.close();
		} catch (Exception ignored)
		{
		}
		setTDWColors();
	}
}
