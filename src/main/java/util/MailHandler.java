package util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;

public class MailHandler {
    static final String ENCODING = "UTF-8";

//    public static void main(String args[]) throws UnsupportedEncodingException, javax.mail.MessagingException {
//        String subject = "Subject";
//        String content = "test";
//        String smtpHost = "smtp-relay.gmail.com";
//        String address = "mail.test19700101@gmail.com";
//        String login = "mail.test19700101";
//        String password = "19700101";
//        String smtpPort = "25";
//
//        sendSimpleMessage(login, password, address, address, content, smtpHost, smtpPort,
//                subject);
//    }

    public static void sendSimpleMessage(String login, String password, String from,
                                         String to,
                                         String content, String smtpHost, String smtpPort,
                                         String subject) throws UnsupportedEncodingException, javax.mail.MessagingException {

        Authenticator auth = new MyAuthenticator(login, password);

        Properties properts = System.getProperties();
        properts.put("mail.smtp.port", smtpPort);
        properts.put("mail.smtp.host", smtpHost);
        properts.put("mail.smtp.auth", "true");
        properts.put("mail.smtp.starttls.enable", "true");
        properts.put("mail.mime.charset", ENCODING);

        Session session = Session.getDefaultInstance(properts, auth);

        Message messaga = new MimeMessage(session);
        messaga.setFrom(new InternetAddress(from));
        messaga.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        messaga.setSubject(subject);
        messaga.setText(content);
        Transport.send(messaga);
    }

    public static void sendMultuplyMessage(String attachement, String login, String password, String from,
                                           String to,
                                           String content, String smtpHost, String smtpPort,
                                           String subject) throws UnsupportedEncodingException, javax.mail.MessagingException {

        Properties multiProps = System.getProperties();
        Authenticator auth = new MyAuthenticator(login, password);

        multiProps.put("mail.smtp.port", smtpPort);
        multiProps.put("mail.smtp.host", smtpHost);
        multiProps.put("mail.smtp.auth", "true");
        multiProps.put("mail.smtp.starttls.enable", "true");
        multiProps.put("mail.mime.charset", ENCODING);

        Session session = Session.getDefaultInstance(multiProps, auth);

        MimeMessage mltPartMsg = new MimeMessage(session);

        mltPartMsg.setFrom(new InternetAddress(from));
        mltPartMsg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        mltPartMsg.setSubject(subject, ENCODING);

        BodyPart msgBodyPart = new MimeBodyPart();
        msgBodyPart.setContent(content, "text/plain; charset = \"" + ENCODING + "\"");
        Multipart multip = new MimeMultipart();
        multip.addBodyPart(msgBodyPart);

        MimeBodyPart atachBodyPart = new MimeBodyPart();

        DataSource atachSource = new FileDataSource(attachement);

        atachBodyPart.setDataHandler(new DataHandler(atachSource));
        atachBodyPart.setFileName(MimeUtility.encodeText(atachSource.getName()));
        multip.addBodyPart(atachBodyPart);

        mltPartMsg.setContent(multip);

        Transport.send(mltPartMsg);
    }

    public static void receiveMessage(String user, String host, String password) throws MessagingException, IOException {

        Authenticator auth = new MyAuthenticator(user, password);

        Properties propes = System.getProperties();
        propes.put("mail.user", user);
        propes.put("mail.host", host);
        propes.put("mail.debug", "false");
        propes.put("mail.store.protocol", "pop3");
        propes.put("mail.transport.protocol", "smtp");

        Session session = Session.getDefaultInstance(propes, auth);

        Store store = session.getStore();
        store.connect();

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);

        Message[] messages = inbox.getMessages();

        ArrayList<String> attach = new ArrayList<String>();

        LinkedList<MessageBean> mailList = getPart(messages, attach);

        inbox.close(false);
        store.close();
    }

    private static LinkedList<MessageBean> getPart(Message[] messages,
                                                   ArrayList<String> attach) throws MessagingException, IOException {
        LinkedList<MessageBean> mailList = new LinkedList<MessageBean>();
        SimpleDateFormat form = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        for (Message mail :
                messages) {
            attach.clear();

            if (mail.isMimeType("text/plain")) {
                MessageBean messageBean = new MessageBean(mail.getMessageNumber(),
                        MimeUtility.decodeText(mail.getSubject()), mail.getFrom()[0]
                        .toString(), null, form.format(mail.getSentDate()), (String)
                        mail.getContent(), null);
                mailList.add(messageBean);
            } else if (mail.isMimeType("multipart/*")) {
                Multipart multi = (Multipart) mail.getContent();
                MessageBean messageBean = null;

                for (int i = 0; i < multi.getCount(); i++) {
                    Part part = multi.getBodyPart(i);
                    if ((part.getFileName() == null || part.getFileName().equals(""))
                            && part.isMimeType("text/plain")) {
                        messageBean = new MessageBean(mail.getMessageNumber(), mail.getSubject(),
                                mail.getFrom()[0].toString(), null, form.format(mail.getSentDate()), (String)
                                mail.getContent(), null);
                    } else {
                        if ((part.getDescription() != null) && part.getDescription()
                                .equals(Part.ATTACHMENT)) {
                            attach.add(saveFile(MimeUtility.decodeText(part.getFileName()),
                                    part.getInputStream()));
                            if (messageBean != null) {
                                messageBean.setAttach(attach);
                            }
                        }
                    }
                }
                mailList.add(messageBean);
            }
        }
        return mailList;
    }

    private static String saveFile(String filename, InputStream inputStream) throws IOException {
        String path = "attachements\\" + filename;
        FileOutputStream out = null;
        try {
            byte[] attachment = new byte[inputStream.available()];
            inputStream.read(attachment);
            File file = new File(path);
            out = new FileOutputStream(file);
            out.write(attachment);
        } finally {
            inputStream.close();
            assert out != null;
            out.close();
            return path;
        }
    }
}




























