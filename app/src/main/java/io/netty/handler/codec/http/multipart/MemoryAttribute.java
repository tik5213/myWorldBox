package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import java.io.IOException;
import java.nio.charset.Charset;

public class MemoryAttribute extends AbstractMemoryHttpData implements Attribute {
    public MemoryAttribute(String name) {
        this(name, HttpConstants.DEFAULT_CHARSET);
    }

    public MemoryAttribute(String name, long definedSize) {
        this(name, definedSize, HttpConstants.DEFAULT_CHARSET);
    }

    public MemoryAttribute(String name, Charset charset) {
        super(name, charset, 0);
    }

    public MemoryAttribute(String name, long definedSize, Charset charset) {
        super(name, charset, definedSize);
    }

    public MemoryAttribute(String name, String value) throws IOException {
        this(name, value, HttpConstants.DEFAULT_CHARSET);
    }

    public MemoryAttribute(String name, String value, Charset charset) throws IOException {
        super(name, charset, 0);
        setValue(value);
    }

    public HttpDataType getHttpDataType() {
        return HttpDataType.Attribute;
    }

    public String getValue() {
        return getByteBuf().toString(getCharset());
    }

    public void setValue(String value) throws IOException {
        if (value == null) {
            throw new NullPointerException("value");
        }
        byte[] bytes = value.getBytes(getCharset());
        checkSize((long) bytes.length);
        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        if (this.definedSize > 0) {
            this.definedSize = (long) buffer.readableBytes();
        }
        setContent(buffer);
    }

    public void addContent(ByteBuf buffer, boolean last) throws IOException {
        int localsize = buffer.readableBytes();
        checkSize(this.size + ((long) localsize));
        if (this.definedSize > 0 && this.definedSize < this.size + ((long) localsize)) {
            this.definedSize = this.size + ((long) localsize);
        }
        super.addContent(buffer, last);
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Attribute)) {
            return false;
        }
        return getName().equalsIgnoreCase(((Attribute) o).getName());
    }

    public int compareTo(InterfaceHttpData other) {
        if (other instanceof Attribute) {
            return compareTo((Attribute) other);
        }
        throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + other.getHttpDataType());
    }

    public int compareTo(Attribute o) {
        return getName().compareToIgnoreCase(o.getName());
    }

    public String toString() {
        return getName() + '=' + getValue();
    }

    public Attribute copy() {
        ByteBuf content = content();
        return replace(content != null ? content.copy() : null);
    }

    public Attribute duplicate() {
        ByteBuf content = content();
        return replace(content != null ? content.duplicate() : null);
    }

    public Attribute retainedDuplicate() {
        ByteBuf content = content();
        if (content == null) {
            return replace(null);
        }
        content = content.retainedDuplicate();
        boolean success = false;
        try {
            Attribute duplicate = replace(content);
            success = true;
            return duplicate;
        } finally {
            if (!success) {
                content.release();
            }
        }
    }

    public Attribute replace(ByteBuf content) {
        MemoryAttribute attr = new MemoryAttribute(getName());
        attr.setCharset(getCharset());
        if (content != null) {
            try {
                attr.setContent(content);
            } catch (Throwable e) {
                throw new ChannelException(e);
            }
        }
        return attr;
    }

    public Attribute retain() {
        super.retain();
        return this;
    }

    public Attribute retain(int increment) {
        super.retain(increment);
        return this;
    }

    public Attribute touch() {
        super.touch();
        return this;
    }

    public Attribute touch(Object hint) {
        super.touch(hint);
        return this;
    }
}
