package com.fwms.basedevss.base.sfs;


import com.fwms.basedevss.base.util.Initializable;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

public interface StaticFileStorage extends Initializable {
    OutputStream create(String file);
    OutputStream createNoThrow(String file);
    InputStream read(String file);
    InputStream read(String file, HttpServletResponse resp);
    InputStream readNoThrow(String file);
    boolean delete(String file);
    boolean exists(String file);
}
