package com.jordanmarques.api.model;

import java.util.HashMap;
import java.util.Map;

public class Tier {
    public static Map<String, Integer> getTiers(){

        HashMap<String, Integer> map = new HashMap<>();
        map.put("BRONZE",1);
        map.put("SILVER",2);
        map.put("GOLD",3);
        map.put("PLATINUM",4);
        map.put("DIAMOND",5);
        map.put("MASTER",6);
        map.put("CHALLENGER",7);

        return map;
    }
}
