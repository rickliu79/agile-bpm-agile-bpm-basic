package com.dstz.bo;

import com.dstz.sys.api.groovy.IScript;
import org.springframework.stereotype.Repository;

@Repository
public class MyTestScript implements IScript {
    public Object max(Object obj) {

        return obj + "名字";
    }
}
