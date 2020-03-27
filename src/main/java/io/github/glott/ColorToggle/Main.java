package io.github.glott.ColorToggle;

public class Main
{
    public static void main(String[] args)
    {
        AirportUpdater.updateAirports();
        Display display = new Display();
        display.run();
    }
}
