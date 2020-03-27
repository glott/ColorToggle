package io.github.glott.ColorToggle;

import io.github.glott.NavDataUpdater.DataHandler;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class Display
{
    private JPanel panel;

    private JButton TCWButton;
    private JButton TDWButton;
    private JButton DODButton;
    private JButton updateNavDataButton;

    private DataHandler dh;
    private io.github.glott.NavDataUpdater.Display navDisplay;
    private SwingWorker worker;

    public Display()
    {
        setButtonColors();
        TCWButton.addActionListener(e -> setColors(0));
        TDWButton.addActionListener(e -> setColors(1));
        DODButton.addActionListener(e -> setColors(2));

        navDisplay = new io.github.glott.NavDataUpdater.Display();
        dh = new DataHandler(navDisplay.getExceptions());
        updateNavDataButton.addActionListener(e ->
                {
                    worker = new SwingWorker<Void, Void>()
                    {
                        @Override
                        protected Void doInBackground() throws Exception
                        {
                            updateNavDataButton.setEnabled(false);
                            try
                            {
                                dh.updateAirports();
                                dh.updateWaypoints();
                                AirportUpdater.updateAirports();
                            } catch (Exception ignored)
                            {
                                updateNavDataButton.setEnabled(true);
                            }
                            return null;
                        }

                        @Override
                        protected void done() { updateNavDataButton.setEnabled(true); }
                    };
                    worker.execute();
                }
        );
    }

    public void run()
    {
        JFrame frame = new JFrame("ColorToggle");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void setButtonColors()
    {
        String s = "";
        try
        {
            File f = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\vSTARS\\vSTARSConfig.xml");
            FileInputStream fis = new FileInputStream(f);
            byte[] data = new byte[(int) f.length()];
            fis.read(data);
            fis.close();

            s = new String(data, StandardCharsets.UTF_8);
        } catch (Exception ignored) {}

        if (s.contains("ColorVideoMapsA Red=\"255\" Green=\"255\" Blue=\"0\""))
        {
            TDWButton.setBackground(new Color(46, 204, 113));
            TCWButton.setBackground(new Color(227, 227, 227));
            DODButton.setBackground(new Color(227, 227, 227));
        } else if (s.contains("ColorVideoMapsB Red=\"0\" Green=\"255\" Blue=\"255\""))
        {
            TDWButton.setBackground(new Color(227, 227, 227));
            TCWButton.setBackground(new Color(227, 227, 227));
            DODButton.setBackground(new Color(46, 204, 113));
        } else
        {
            TDWButton.setBackground(new Color(227, 227, 227));
            TCWButton.setBackground(new Color(46, 204, 113));
            DODButton.setBackground(new Color(227, 227, 227));
        }
    }

    private void setColors(int i)
    {
        try
        {
            File f = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\vSTARS\\vSTARSConfig.xml");
            FileInputStream fis = new FileInputStream(f);
            byte[] data = new byte[(int) f.length()];
            fis.read(data);
            fis.close();

            String s = new String(data, StandardCharsets.UTF_8);

            if (i == 0)
            {
                s = s.replaceFirst("ColorVideoMapsA.*", "ColorVideoMapsA Red=\"140\" Green=\"140\" Blue=\"140\" />");
                s = s.replaceFirst("ColorVideoMapsB.*", "ColorVideoMapsB Red=\"140\" Green=\"140\" Blue=\"140\" />");
            } else if (i == 1)
            {
                s = s.replaceFirst("ColorVideoMapsA.*", "ColorVideoMapsA Red=\"255\" Green=\"255\" Blue=\"0\" />");
                s = s.replaceFirst("ColorVideoMapsB.*", "ColorVideoMapsB Red=\"255\" Green=\"255\" Blue=\"0\" />");
            } else
            {
                s = s.replaceFirst("ColorVideoMapsA.*", "ColorVideoMapsA Red=\"140\" Green=\"140\" Blue=\"140\" />");
                s = s.replaceFirst("ColorVideoMapsB.*", "ColorVideoMapsB Red=\"0\" Green=\"255\" Blue=\"255\" />");
            }

            BufferedWriter out = new BufferedWriter(new FileWriter(f), 32768);
            out.write(s);
            out.close();

        } catch (Exception ignored) {}

        setButtonColors();
    }
}
