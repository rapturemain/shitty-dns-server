package org.rapturemain.simplednsserver;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Header {

    public static int LENGTH = 12;

    private int id;

    private int qr;
    private int opcode;
    private int aa;
    private int tc;
    private int rd;
    private int ra;
    private int z;
    private int rcode;

    private int qdcount;
    private int ancount;
    private int nscount;
    private int arcount;

    public void writeBytes(byte[] bytes) {
        bytes[0] = (byte) ((id & 0xFF00) >> 8);
        bytes[1] = (byte) (id & 0x00FF);

        int flags = (qr << 15) | (opcode << 11) | (aa << 10) | (tc << 9) | (rd << 8) | (ra << 7) | (z << 4) | rcode;
        bytes[2] = (byte) ((flags & 0xFF00) >> 8);
        bytes[3] = (byte) (flags & 0x00FF);

        bytes[4] = (byte) ((qdcount & 0xFF00) >> 8);
        bytes[5] = (byte) (qdcount & 0x00FF);

        bytes[6] = (byte) ((ancount & 0xFF00) >> 8);
        bytes[7] = (byte) (ancount & 0x00FF);

        bytes[8] = (byte) ((nscount & 0xFF00) >> 8);
        bytes[9] = (byte) (nscount & 0x00FF);

        bytes[10] = (byte) ((arcount & 0xFF00) >> 8);
        bytes[11] = (byte) (arcount & 0x00FF);
    }

    public static Header fromBytes(byte[] bytes) {
        Header.HeaderBuilder builder = Header.builder();
        builder.id = (bytes[0] << 8) | bytes[1];

        int flags = (bytes[2] << 8) | bytes[3];
        builder.qr = (flags & 0x8000) >> 15;
        builder.opcode = (flags & 0x78000) >> 11;
        builder.aa = (flags & 0x0400) >> 10;
        builder.tc = (flags & 0x0200) >> 9;
        builder.rd = (flags & 0x0100) >> 8;
        builder.ra = (flags & 0x0080) >> 7;
        builder.z = (flags & 0x0070) >> 4;
        builder.rcode = flags & 0x000F;

        builder.qdcount = (bytes[4] << 8) | bytes[5];
        builder.ancount = (bytes[6] << 8) | bytes[7];
        builder.nscount = (bytes[8] << 8) | bytes[9];
        builder.arcount = (bytes[10] << 8) | bytes[11];

        return builder.build();
    }
}
