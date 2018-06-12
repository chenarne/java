package com.fwms.basedevss.base.rpc;


import com.fwms.basedevss.ServerException;
import com.fwms.basedevss.base.BaseErrors;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.commons.lang.ObjectUtils;

import java.io.IOException;

public class RPCClient {
    protected final TransceiverFactory transceiverFactory;

    public RPCClient(TransceiverFactory tf) {
        this.transceiverFactory = tf;
    }

    public TransceiverFactory getTransceiverFactory() {
        return transceiverFactory;
    }

    protected Transceiver getTransceiver(Class ifaceClass) {
        return transceiverFactory.getTransceiver(ifaceClass);
    }

    protected static void closeTransceiver(Transceiver trans) {
        try {
            trans.close();
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_TRANSCEIVER_CLOSE, e);
        }
    }

    protected static <T> T getProxy(Class<? extends T> ifaceClass, Transceiver trans) {
        try {
            return SpecificRequestor.getClient(ifaceClass, trans);
        } catch (IOException e) {
            throw new ServerException(BaseErrors.PLATFORM_GET_PROXY_ERROR, e);
        }
    }

    protected static String toStr(Object o) {
        return ObjectUtils.toString(o, "");
    }
}
