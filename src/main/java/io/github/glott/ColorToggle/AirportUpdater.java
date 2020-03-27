package io.github.glott.ColorToggle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AirportUpdater
{

    public static void updateAirports()
    {
        File f = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\vSTARS\\Airports.xml");

        InputStream is = AirportUpdater.class.getResourceAsStream("Replacements.txt");
        Scanner sc = new Scanner(is).useDelimiter("\\A");
        String replace = sc.hasNext() ? sc.next() : "";

        try
        {
            if (f.exists())
            {
                FileInputStream fis = new FileInputStream(f);
                byte[] data = new byte[(int) f.length()];
                fis.read(data);
                fis.close();
                String s = new String(data, StandardCharsets.UTF_8);

                if (!s.contains("KSFN"))
                {
                    s = s.replaceFirst("</Airport>", "</Airport>\n" + replace);

                    BufferedWriter out = new BufferedWriter(new FileWriter(f), 32768);
                    out.write(s);
                    out.close();
                }
            }
        } catch (Exception ignored) {}
    }

}
