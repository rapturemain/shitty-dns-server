package org.rapturemain.simplednsserver;

import lombok.*;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AAAARecord extends Record {

    public void setAddr(String s) {
        String[] parts = s.split("\\.");
        if (parts.length != 16) {
            throw new IllegalArgumentException();
        }

        byte[] bytes = new byte[16];

        for (int i = 0; i < 16; i++) {
            String part = parts[i];
            long p = Long.parseLong(part);
            if (p > 255 || p < 0) {
                throw new IllegalArgumentException();
            }
            bytes[i] = (byte) p;
        }

        super.rdata = bytes;
        super.rdlength = bytes.length;
    }
}
