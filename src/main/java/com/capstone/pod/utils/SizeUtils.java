package com.capstone.pod.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SizeUtils {
    public static Map<String , Integer> sizes = new HashMap<>(){{
        put("XS" , 0);
        put("S" , 1);
        put("M" , 2);
        put("L" , 3);
        put("XL" , 4);
        put("2Xl" , 5);
        put("XXL" , 6);
        put("3XL" , 7);
        put("XXXL" , 8);


    }};

    public static List<String> sortSize(List<String> sizeInput){
        for (int i = 0 ; i < sizeInput.size() ; i++){
            String sizeTemp = null;
            for (int j = i+1 ; j < sizeInput.size() ; j++){
                if(sizes.get(sizeInput.get(i)) != null && sizes.get(sizeInput.get(j)) != null){
                    if(sizes.get(sizeInput.get(i)) > sizes.get(sizeInput.get(j))){
                        sizeTemp = new String(sizeInput.get(i));
                        sizeInput.set(i , sizeInput.get(j)) ;
                        sizeInput.set(j , sizeTemp);
                    }
                }
            }
        }
        return  sizeInput;
    }

}
