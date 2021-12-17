package org.rapturemain.simplednsserver;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MXRecord extends Record {

    private int exchange;
    private Name mName;

    {
        mName = new Name("");
        exchange = 100;
    }

    public void setExchange(int exchange) {
        this.exchange = exchange;

        writeMName();
        writeExchange();
    }

    public void setMName(Name mName) {
        this.mName = mName;

        writeMName();
        writeExchange();
    }

    private void writeMName() {
        byte[] bytes = new byte[mName.length + 2];
        mName.writeBytes(bytes, 2);

        super.rdata = bytes;
        super.rdlength = bytes.length;
    }

    private void writeExchange() {
        byte[] old = super.rdata;
        byte[] bytes = new byte[old.length];

        bytes[0] = (byte) ((exchange >> 8) & 0xFF);
        bytes[1] = (byte) (exchange & 0xFF);
        System.arraycopy(old, 2, bytes, 2, old.length - 2);

        super.rdata = bytes;
        super.rdlength = rdata.length;
    }
}
