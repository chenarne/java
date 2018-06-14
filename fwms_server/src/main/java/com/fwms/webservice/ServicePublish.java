package com.fwms.webservice;

import com.fwms.webservice.entity.Constants;

import javax.xml.ws.Endpoint;

/**
 * Created by chenarne on 2017/3/2.
 */

public class ServicePublish {
    public static void publish() {
        Endpoint.publish(Constants.address, new OrderServiceImpl());
    }
}
