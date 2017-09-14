package util;

import java.io.Serializable;
import java.util.ArrayList;

public class MessageBean implements Serializable {

    private String subject;
    private String from;
    private String to;
    private String dateSent;
    private String contenеt;
    private int msgId;
    private ArrayList<String> attach;

    public MessageBean(int messageNumber, String subject, String from, String to, String
            dateSent, String content, ArrayList<String> attach) {
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.dateSent = dateSent;
        this.contenеt = content;
        this.msgId = messageNumber;
        this.attach = attach;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public String getContenеt() {
        return contenеt;
    }

    public void setContenеt(String contenеt) {
        this.contenеt = contenеt;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public ArrayList<String> getAttach() {
        return attach;
    }

    public void setAttach(ArrayList<String> attach) {
        this.attach = attach;
    }

    public MessageBean() {
    }

}
