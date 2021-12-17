package org.rapturemain.simplednsserver;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PTRRecord extends Record {
    protected Name pName;

    public void setPName(Name name) {
        this.pName = name;

        byte[] bytes = new byte[name.length];
        name.writeBytes(bytes, 0);

        super.rdata = bytes;
        super.rdlength = bytes.length;
    }
}
