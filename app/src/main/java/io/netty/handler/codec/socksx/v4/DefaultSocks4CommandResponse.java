package io.netty.handler.codec.socksx.v4;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.NetUtil;
import io.netty.util.internal.StringUtil;

public class DefaultSocks4CommandResponse extends AbstractSocks4Message implements Socks4CommandResponse {
    private final String dstAddr;
    private final int dstPort;
    private final Socks4CommandStatus status;

    public DefaultSocks4CommandResponse(Socks4CommandStatus status) {
        this(status, null, 0);
    }

    public DefaultSocks4CommandResponse(Socks4CommandStatus status, String dstAddr, int dstPort) {
        if (status == null) {
            throw new NullPointerException("cmdStatus");
        } else if (dstAddr != null && !NetUtil.isValidIpV4Address(dstAddr)) {
            throw new IllegalArgumentException("dstAddr: " + dstAddr + " (expected: a_isRightVersion valid IPv4 address)");
        } else if (dstPort < 0 || dstPort > 65535) {
            throw new IllegalArgumentException("dstPort: " + dstPort + " (expected: 0~65535)");
        } else {
            this.status = status;
            this.dstAddr = dstAddr;
            this.dstPort = dstPort;
        }
    }

    public Socks4CommandStatus status() {
        return this.status;
    }

    public String dstAddr() {
        return this.dstAddr;
    }

    public int dstPort() {
        return this.dstPort;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder(96);
        buf.append(StringUtil.simpleClassName((Object) this));
        DecoderResult decoderResult = decoderResult();
        if (decoderResult.isSuccess()) {
            buf.append("(dstAddr: ");
        } else {
            buf.append("(decoderResult: ");
            buf.append(decoderResult);
            buf.append(", dstAddr: ");
        }
        buf.append(dstAddr());
        buf.append(", dstPort: ");
        buf.append(dstPort());
        buf.append(')');
        return buf.toString();
    }
}
