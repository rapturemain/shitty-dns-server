package org.rapturemain.simplednsserver;

import lombok.*;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Name {

    public Name(String str) {
        String[] parts = str.split("\\.");

        int length = 1;
        byte[][] labels = new byte[parts.length][];
        for (int j = 0; j < parts.length; j++) {
            String p = parts[j];

            byte[] label = new byte[p.length()];
            for (int i = 0; i < p.length(); i++) {
                label[i] = (byte) p.charAt(i);
                length++;
            }
            length++;

            labels[j] = label;
        }

        this.length = length;
        this.labels = labels;
    }

    protected int length;
    protected byte[][] labels;

    public void writeBytes(byte[] bytes, int start) {
        int pos = start;

        for (byte[] label : labels) {
            bytes[pos++] = (byte) label.length;
            System.arraycopy(label, 0, bytes, pos, label.length);
            pos += label.length;
        }
        bytes[pos] = 0;
    }

    public static Name fromBytes(byte[] bytes, int start) {
        int pos = start;
        int len = 1;

        ArrayList<byte[]> labels = new ArrayList<>();
        int nextLen;
        while ((nextLen = bytes[pos++]) != 0) {
            byte[] nextLabel = new byte[nextLen];
            System.arraycopy(bytes, pos, nextLabel, 0, nextLen);
            labels.add(nextLabel);
            pos += nextLen;
            len += nextLen + 1;
        }

        return Name.builder()
                .length(len)
                .labels(labels.toArray(new byte[0][]))
                .build();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (byte[] label : labels) {
            for (byte b : label) {
                builder.append((char) b);
            }
            builder.append('.');
        }
        return builder.toString();
    }
}
