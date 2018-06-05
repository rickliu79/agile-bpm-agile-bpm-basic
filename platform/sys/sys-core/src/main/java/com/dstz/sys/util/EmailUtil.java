package com.dstz.sys.util;

import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.string.StringUtil;
import com.dstz.sys.email.MailUtil;
import com.dstz.sys.email.model.Mail;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 这个类用于发送邮件。
 * <pre>
 * </pre>
 */
public class EmailUtil {


    /**
     * 发送邮件。
     * <pre>
     * 	调用方法：
     * 	EmailUtil.sendEmail("收信人地址","抄送人地址","秘密抄送","发送人地址","主题","邮件内容");
     * </pre>
     *
     * @param to      收件人
     * @param cc      抄送人
     * @param bcc     密送人
     * @param from    发件人
     * @param subject 标题
     * @param content 内容
     * @throws MessagingException void
     */
    public static void sendEmail(String to, String cc, String bcc, String from, String subject, String content) throws MessagingException {
        MailUtil mailSender = (MailUtil) AppUtil.getBean("mailSender");
        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.setSenderName(from);
        mail.setContent(content);
        mail.setReceiverAddresses(to);
        mail.setSenderAddress(from);
        if (StringUtil.isNotEmpty(cc)) {
            mail.setCopyToAddresses(cc);
        }

        if (StringUtil.isNotEmpty(bcc)) {
            mail.setBcCAddresses(bcc);
        }
        mail.setSendDate(new Date());
        try {
            mailSender.send(mail);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
