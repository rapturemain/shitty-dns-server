package org.rapturemain.simplednsserver;

import lombok.*;

import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class TXTRecord extends Record {

    private String txt;

    public void setTxt(String txt) {
        byte[] bytes = txt.getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[bytes.length + 1];
        actual[0] = (byte) (bytes.length);
        System.arraycopy(bytes, 0, actual, 1, bytes.length);

        super.rdata = actual;
        super.rdlength = actual.length;
    }

}
