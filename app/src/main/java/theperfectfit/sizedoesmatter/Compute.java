package theperfectfit.sizedoesmatter;

import Structs.FloatPoint;

public class Compute {

    public static double length(FloatPoint p1, FloatPoint p2){

        float x1 = p1.x;
        float y1 = p1.y;
        float x2 = p2.x;
        float y2 = p2.y;

        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }

}
