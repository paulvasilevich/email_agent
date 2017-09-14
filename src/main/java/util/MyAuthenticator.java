package util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


public class MyAuthenticator extends Authenticator {
    private String user;
    private String password;

    public MyAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public javax.mail.PasswordAuthentication getPasswordAuthentication() {
        String user = this.user;
        String password = this.password;
        return new PasswordAuthentication(user, password);
    }
}
