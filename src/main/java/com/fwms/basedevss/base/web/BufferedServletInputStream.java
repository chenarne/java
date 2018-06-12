package com.fwms.basedevss.base.web;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;

/**
 * Created by acer01 on 2016/4/30/030.
 */
public class BufferedServletInputStream extends ServletInputStream {

    ByteArrayInputStream bais;

    public BufferedServletInputStream(ByteArrayInputStream bais) {
        this.bais = bais;
    }

    public int available() {
        return bais.available();
    }

    public int read() {
        return bais.read();
    }

    public int read(byte[] buf, int off, int len) {
        return bais.read(buf, off, len);
    }
}
