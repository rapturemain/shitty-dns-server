import org.rapturemain.simplednsserver.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(53);

        while (true) {
            byte[] bytes = new byte[512];
            DatagramPacket packet = new DatagramPacket(bytes, 512);
            socket.receive(packet);

            Message m = processMessage(Message.fromBytes(bytes));

            byte[] ans = m.writeBytes();
            DatagramPacket sendPacket = new DatagramPacket(ans, ans.length, packet.getAddress(), packet.getPort());

            socket.send(sendPacket);
        }
    }

    private static Message processMessage(Message message) {
        System.out.println("- - Received - -");
        System.out.println(message);

        Message m = message;
        m.getHeader().setQr(1);
        m.getHeader().setRa(0);
        for (int i = m.getAnswers().size(); i < m.getQuestions().size(); i++) {
            Question q = m.getQuestions().get(i);
            try {
                Record ans = processQuestion(q);
                if (ans != null) {
                    message.getAnswers().add(ans);
                } else {
                    m.getHeader().setRcode(4);
                    break;
                }
            } catch (NoNameException e) {
                m.getHeader().setRcode(3);
                break;
            }
        }
        m.getHeader().setAncount(m.getAnswers().size());

        System.out.println("- - Processed - -");
        System.out.println(m);
        return m;
    }

    private static Record processQuestion(Question q) throws NoNameException {
        if (!q.getQname().equals(new Name("test.rapture")) && q.getQtype() != 12) {
            throw new NoNameException();
        }
        switch (q.getQtype()) {
            case 12: // PTR
                PTRRecord ptrR = new PTRRecord();
                ptrR.setTtl(1000 * 60 * 60 * 24);
                ptrR.setType(q.getQtype());
                ptrR.setClazz(q.getQclass());
                ptrR.setName(q.getQname());
                ptrR.setPName(new Name("dns.test.rapture"));
                ptrR.computeLength();
                return ptrR;
            case 1: // A
                ARecord aR = new ARecord();
                aR.setTtl(1000 * 60 * 60 * 24);
                aR.setType(q.getQtype());
                aR.setClazz(q.getQclass());
                aR.setName(q.getQname());
                aR.setInetAddress((192L << 24) | (64L << 16) | 1);
                aR.computeLength();
                return aR;
            case 15: // MX
                MXRecord mR = new MXRecord();
                mR.setTtl(1000 * 60 * 60 * 24);
                mR.setType(q.getQtype());
                mR.setClazz(q.getQclass());
                mR.setName(q.getQname());
                mR.setExchange(0);
                mR.setMName(new Name("mx.test.rapture"));
                mR.computeLength();
                return mR;
            case 16: // TXT
                TXTRecord txtR = new TXTRecord();
                txtR.setTtl(1000 * 60 * 60 * 24);
                txtR.setType(q.getQtype());
                txtR.setClazz(q.getQclass());
                txtR.setName(q.getQname());
                txtR.setTxt("Ahah, some shitty DNS server here");
                txtR.computeLength();
                return txtR;
            case 28: // AAAA
                AAAARecord aaaaR = new AAAARecord();
                aaaaR.setTtl(1000 * 60 * 60 * 24);
                aaaaR.setType(q.getQtype());
                aaaaR.setClazz(q.getQclass());
                aaaaR.setName(q.getQname());
                aaaaR.setAddr("1.0.0.0.1.128.254.0.125.77.65.12.15.64.72.53");
                aaaaR.computeLength();
                return aaaaR;
        }

        return null;
    }
}
