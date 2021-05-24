package io.github.glott.ColorToggle;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AirportUpdater
{

    private static final String REP_DATE = "052321";
    private static final File AIRPORTS = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\vSTARS\\Airports.xml");

    public static void updateAirports()
    {
        InputStream is;
        try
        {
            is = new URL("https://joshglott.com/vaf/toggle_replacements.txt").openStream();
        } catch (Exception ex)
        {
            is = AirportUpdater.class.getResourceAsStream("Replacements.txt");
        }

        Scanner sc = new Scanner(is).useDelimiter("\\A");
        String replacement = sc.hasNext() ? sc.next() : "";

        try
        {
            if (AIRPORTS.exists())
            {
                FileInputStream fis = new FileInputStream(AIRPORTS);
                byte[] data = new byte[(int) AIRPORTS.length()];
                fis.read(data);
                fis.close();

                pushUpdate(data, replacement);
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static void pushUpdate(byte[] data, String replacement)
    {
        String s = new String(data, StandardCharsets.UTF_8);

        if (!s.contains("REP=\"" + REP_DATE + "\""))
        {
            s = s.replaceFirst("</Airport>", "</Airport>\n" + replacement);

            try
            {
                BufferedWriter out = new BufferedWriter(new FileWriter(AIRPORTS), 32768);
                out.write(s);
                out.close();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

}
