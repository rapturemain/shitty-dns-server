package org.rapturemain.simplednsserver;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ARecord extends Record {

    protected long inetAddress;

    public void setInetAddress(long inetAddress) {
        this.inetAddress = inetAddress;
        super.rdata = new byte[4];
        super.rdata[0] = (byte) ((inetAddress >> 24) & 0xFF);
        super.rdata[1] = (byte) ((inetAddress >> 16) & 0xFF);
        super.rdata[2] = (byte) ((inetAddress >> 8) & 0xFF);
        super.rdata[3] = (byte) (inetAddress & 0xFF);
    }

    public static ARecord fromRecord(Record r) {
        long inetAddress = (r.rdata[0] << 24) | (r.rdata[1]  << 16) | (r.rdata[2] << 8) | r.rdata[3] ;

        ARecord aRecord = new ARecord();

        aRecord.inetAddress = inetAddress;
        aRecord.length = r.length;
        aRecord.name = r.name;
        aRecord.type = r.type;
        aRecord.clazz = r.clazz;
        aRecord.ttl = r.ttl;
        aRecord.rdata = r.rdata;
        aRecord.rdlength = r.rdlength;

        return aRecord;
    }
}
