package com.workspace.crm.commons.utils;

import java.util.UUID;

public class IdUtils {
    //生成UUID
    public static String createId(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
