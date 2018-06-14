package com.fwms;
import java.io.File;
import java.io.IOException;

import com.fwms.basedevss.base.web.JettyServer;
import com.fwms.webservice.ServicePublish;


public class ServerStart {

    public static void main(String[] args) throws IOException {
        String prop_file = "";

        for (String arg : args){
            if (arg.contains("properties"))  {
                prop_file = arg;
                break;
            }
        }

        File file0 = new File(prop_file);
        if (!file0.exists()) {
            prop_file = "fwms.properties";
        }

        JettyServer.main(new String[]{"-c", "file://" + prop_file});
    }
}
