package com.talnex.wrongsbook.Utils;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorUtils {

    public static int getRandColor() {
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return color;
    }

    //public static int rank0 = Color.parseColor("#FF0000");
    public static int rank1 = Color.parseColor("#FF7F50");
    public static int rank2 = Color.parseColor("#FFD700");
    public static int rank3 = Color.parseColor("#7FFF00");
    public static int rank4 = Color.parseColor("#40E0D0");
    public static int rank5 = Color.parseColor("#DA70D6");

    public static List<Integer> rankColor = new ArrayList<>();

    public ColorUtils() {
        //rankColor.add(rank0);
        rankColor.add(rank1);
        rankColor.add(rank2);
        rankColor.add(rank3);
        rankColor.add(rank4);
        rankColor.add(rank5);
    }

}
