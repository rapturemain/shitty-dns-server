package org.rapturemain.simplednsserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private int length;

    private Name qname;
    private int qtype;
    private int qclass;

    public void writeBytes(byte[] bytes, int start) {
        int pos = start;

        qname.writeBytes(bytes, pos);
        pos += qname.length;

        bytes[pos] = (byte) ((qtype & 0xFF00) >> 8);
        bytes[pos + 1] = (byte) (qtype & 0x00FF);

        bytes[pos + 2] = (byte) ((qclass & 0xFF00) >> 8);
        bytes[pos + 3] = (byte) (qclass & 0x00FF);
    }

    public static Question fromBytes(byte[] bytes, int start) {
        Question.QuestionBuilder builder = Question.builder();
        int pos = start;
        int len = 4;

        builder.qname = Name.fromBytes(bytes, pos);
        len += builder.qname.length;
        pos += builder.qname.length;

        builder.length = len;
        builder.qtype = (bytes[pos] << 8) | bytes[pos + 1];
        builder.qclass = (bytes[pos + 2] << 8) | bytes[pos + 3];

        return builder.build();
    }

}
