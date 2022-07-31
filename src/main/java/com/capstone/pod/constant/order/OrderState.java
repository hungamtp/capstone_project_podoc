package com.capstone.pod.constant.order;

import java.util.Arrays;
import java.util.List;

public final class OrderState {
    public static final String PENDING = "PENDING";
    public static final String PRINTING = "PRINTING";
    public static final String PACKAGING = "PACKAGING";
    public static final String DELIVERING = "DELIVERING";
    public static final String DELIVERED = "DELIVERED";
    public static final String DONE = "DONE";
    public static List<String> getAllOrderState(){
       return Arrays.asList(PENDING,PRINTING,PACKAGING,DELIVERING,DELIVERED,DONE);
    }
}
