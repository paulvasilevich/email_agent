package agent;

import util.MailHandler;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@WebServlet("/sendMail")
public class SendMail extends HttpServlet {

    private static final String SUBJECT = "theme";
    private static final String CONTENT = "message";
    private static final String FROM = "from";
    private static final String TO = "to";
    private static final String ATTACHMENT = "attachFile";
    private static final String PASSWORD = "password";
    private static final String SMTPHOST = "smtp.gmail.com";
    private static final String SMTPPORT = "587";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int indexSobaka = req.getParameter(FROM).indexOf('@');
        String login = req.getParameter(FROM).substring(0, indexSobaka);
        String password = req.getParameter(PASSWORD);
        String from = req.getParameter(FROM);
        String to = req.getParameter(TO);
        String content = req.getParameter(CONTENT);
        String subject = req.getParameter(SUBJECT);
        String url = req.getParameter(ATTACHMENT);
        try {
            if (url == null || url.equals("")) {
                MailHandler.sendSimpleMessage(login, password, from, to, content, SMTPHOST,
                        SMTPPORT,
                        subject);
            } else {
                MailHandler.sendMultuplyMessage(url,login, password, from, to, content, SMTPHOST,
                        SMTPPORT,
                        subject);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
