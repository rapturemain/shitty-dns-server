package org.rapturemain.simplednsserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {
    protected int length;

    protected Name name;
    protected int type;
    protected int clazz;
    protected long ttl;
    protected int rdlength;
    protected byte[] rdata;

    public void computeLength() {
        int len = 2 + 2 + 4 + 2;
        len += name.getLength();
        len += rdata.length;
        this.length = len;
    }

    public void writeBytes(byte[] bytes, int start) {
        int pos = start;

        name.writeBytes(bytes, pos);
        pos += name.length;

        bytes[pos] = (byte) ((type & 0xFF00) >> 8);
        bytes[pos + 1] = (byte) (type & 0x00FF);

        bytes[pos + 2] = (byte) ((clazz & 0xFF00) >> 8);
        bytes[pos + 3] = (byte) (clazz & 0x00FF);

        bytes[pos + 4] = (byte) ((ttl & 0xFF000000) >> 24);
        bytes[pos + 5] = (byte) ((ttl & 0xFF0000) >> 16);
        bytes[pos + 6] = (byte) ((ttl & 0xFF00) >> 8);
        bytes[pos + 7] = (byte) ((ttl & 0xFF));

        bytes[pos + 8] = (byte) ((rdlength & 0xFF00) >> 8);
        bytes[pos + 9] = (byte) (rdlength & 0x00FF);

        System.arraycopy(rdata, 0, bytes, pos + 10, rdata.length);
    }

    public static Record fromBytes(byte[] bytes, int start) {
        Record.RecordBuilder builder = Record.builder();
        int pos = start;
        int len = 10;

        builder.name = Name.fromBytes(bytes, start);
        pos += builder.name.length;
        len += builder.name.length;

        builder.type = (bytes[pos] << 8) | bytes[pos + 1];
        builder.clazz = (bytes[pos + 2] << 8) | bytes[pos + 3];
        builder.ttl = (bytes[pos + 4] << 24) | (bytes[pos + 5] << 16) | (bytes[pos + 6] << 8) | bytes[pos + 7];
        builder.rdlength = (bytes[pos + 8] << 8) | bytes[pos + 9];

        pos = pos + 10;
        builder.rdata = new byte[builder.rdlength];
        System.arraycopy(bytes, pos, builder.rdata, 0, builder.rdlength);

        builder.length = len + builder.rdlength;

        return builder.build();
    }
}
