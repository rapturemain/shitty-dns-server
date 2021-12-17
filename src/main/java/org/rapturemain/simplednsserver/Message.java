package org.rapturemain.simplednsserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    private Header header;
    private List<Question> questions;
    private List<Record> answers;

    public byte[] writeBytes() {
        int pos = 0;

        int len = Header.LENGTH;
        for (Question q: questions) {
            len += q.getLength();
        }
        for (Record r: answers) {
            len += r.getLength();
        }

        byte[] bytes = new byte[len];

        header.writeBytes(bytes);
        pos = Header.LENGTH;

        for (Question q: questions) {
            q.writeBytes(bytes, pos);
            pos += q.getLength();
        }
        for (Record r: answers) {
            len += r.getLength();
            r.writeBytes(bytes, pos);
            pos += r.getLength();
        }

        return bytes;
    }

    public static Message fromBytes(byte[] bytes) {
        int pos = 0;
        Header h = Header.fromBytes(bytes);
        pos += Header.LENGTH;

        List<Question> qs = new ArrayList<>();
        for (int i = 0; i < h.getQdcount(); i++) {
            Question q = Question.fromBytes(bytes, pos);
            pos += q.getLength();
            qs.add(q);
        }

        List<Record> as = new ArrayList<>();
        for (int i = 0; i < h.getAncount(); i++) {
            Record r = Record.fromBytes(bytes, pos);
            pos += r.getLength();
            as.add(r);
        }

        return Message.builder()
                .header(h)
                .questions(qs)
                .answers(as)
                .build();
    }
}
