package uet.oop.bomberman.HelperClass;

import uet.oop.bomberman.entities.Entity;

public class Helper {
    public static double getPreciseDouble(double x)
    {
        return Math.round(x * 10000) / 10000.0;
    }


}
