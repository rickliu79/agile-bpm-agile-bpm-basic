package com.dstz.sys.email;

import com.dstz.base.core.util.StringUtil;
import com.dstz.sys.email.model.Mail;
import com.dstz.sys.email.model.MailAddress;
import com.dstz.sys.email.model.MailAttachment;
import com.dstz.sys.email.model.MailSetting;
import com.sun.mail.imap.IMAPFolder;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.*;
import java.util.Map.Entry;

/**
 * <pre>
 * 描述：邮件处理类，实现邮箱的接收、发送、测试连接功能
 * </pre>
 */
public class MailUtil {
    /**
     * 邮件设置实体类
     */
    private MailSetting mailSetting;
    /**
     * 附件处理接口
     */
    private AttacheHandler handler;

    private static final String CHARSET = "utf-8";

    /**
     * 构造函数
     *
     * @param mailSetting 邮件设置实体类
     *
     *                    <pre>
     *                    接收示例：
     *                    MailSetting setting = getMailSettingEntity();
     *                    MailUtil util = new MailUtil(setting);
     *                    List&lt;Mail&gt; list = util.receive(new AttacheHandler(){……});
     *
     *                    发送示例：
     *                    MailSetting setting = getMailSettingEntity();
     *                    MailUtil util = new MailUtil(setting);
     *                    Mail mail = getMailEntity();
     *                    util.send(mail);
     *
     *                    测试连接示例：
     *                    MailSetting setting = getMailSettingEntity();
     *                    MailUtil util = new MailUtil(setting);
     *                    util.connectSmtpAndReceiver();
     *                    </pre>
     * @see    MailSetting
     * @see AttacheHandler
     * @see Mail
     */
    public MailUtil(MailSetting mailSetting) {
        this.mailSetting = mailSetting;
    }

    /**
     * 测试发送邮件服务器和接收邮件服务器连接情况
     *
     * @throws MessagingException
     */
    public void connectSmtpAndReceiver() throws MessagingException {
        connectSmtp();
        connectReciever();
    }

    /**
     * 测试发送邮件服务器连接情况
     *
     * @throws MessagingException
     */
    public void connectSmtp() throws MessagingException {
        // 取得通道session
        Session session = getMailSession(MailSetting.SMTP_PROTOCAL);
        // 创建smtp连接
        Transport transport = null;
        try {
            transport = session.getTransport(MailSetting.SMTP_PROTOCAL);
            transport.connect(mailSetting.getSendHost(),
                    mailSetting.getMailAddress(), mailSetting.getPassword());
        } catch (MessagingException e) {
            e.printStackTrace();
            throw e;
        } finally {
            transport.close();
        }
    }

    /**
     * 测试接收邮件服务器连接情况
     *
     * @throws MessagingException
     */
    public void connectReciever() throws MessagingException {
        Session session = getMailSession(mailSetting.getProtocal());
        // 创建连接
        Store store = null;
        URLName urln = new URLName(mailSetting.getProtocal(), mailSetting.getReceiveHost(), Integer.parseInt(mailSetting.getReceivePort()),
                null, mailSetting.getMailAddress(), mailSetting.getPassword());
        try {
            store = session.getStore(urln);
            store.connect();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw e;
        } finally {
            store.close();
        }
    }

    /**
     * 发送邮件
     *
     * @param mail 邮件信息实体
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @see    Mail
     */
    public void send(Mail mail) throws UnsupportedEncodingException, MessagingException {
        Session session = getMailSession(MailSetting.SMTP_PROTOCAL);

        MimeMessage message = new MimeMessage(session);
        addAddressInfo(mail, message);

        BodyPart contentPart = new MimeBodyPart();// 内容
        Multipart multipart = new MimeMultipart();
        contentPart.setHeader("Content-Transfer-Encoding", "base64");
        // 邮件正文(第二部分邮件的内容及附件)
        contentPart.setContent(mail.getContent(), "text/html;charset=utf-8");
        message.setSubject(mail.getSubject(), MailUtil.CHARSET);
        message.setText(MailUtil.CHARSET, MailUtil.CHARSET);
        message.setSentDate(new Date());
        multipart.addBodyPart(contentPart);// 邮件正文
        message.setContent(multipart);
        // 添加附件
        for (MailAttachment attachment : mail.getMailAttachments()) {
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = null;
            String filePath = attachment.getFilePath();
            if (filePath == null || "".equals(filePath)) {
                source = new ByteArrayDataSource(attachment.getFileBlob(), "application/octet-stream");
            } else {
                source = new FileDataSource(new File(filePath));
            }
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(MimeUtility.encodeWord(
                    attachment.getFileName(), MailUtil.CHARSET, "Q"));
            multipart.addBodyPart(messageBodyPart);
        }
        message.setContent(multipart);
        message.saveChanges();
        Transport transport = session.getTransport(MailSetting.SMTP_PROTOCAL);
        transport.connect(mailSetting.getSendHost(),
                mailSetting.getMailAddress(), mailSetting.getPassword());
        transport.sendMessage(message, message.getAllRecipients());
        
    }

    /**
     * 接收邮件
     *
     * @param handler 附件处理类
     * @return 邮件信息实体列表
     * @throws Exception
     * @see    AttacheHandler
     */
    public List<Mail> receive(AttacheHandler handler) throws Exception {
        return receive(handler, "");
    }

    /**
     * 接收邮件
     *
     * @param handler             附件处理类
     * @param lastHandleMessageId 最近一次处理后保存的邮件messageId，若为空，则将获取所有邮件；若不为空，则只会获在该邮件之后接收到的邮件
     * @return 邮件信息实体列表
     * @throws Exception
     * @see    AttacheHandler
     */
    public List<Mail> receive(AttacheHandler handler, String lastHandleMessageId) throws Exception {
        this.handler = handler;
        Store connectedStore = getConnectedStore();
        Folder folder = getFolder(connectedStore);
        try {
            return getMessages(folder, lastHandleMessageId);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            close(folder, connectedStore);
        }
    }

    /**
     * 通过messageID，下载邮件
     *
     * @param handler   附件处理类
     * @param messageID 所要下载的邮件messageID
     * @return
     * @throws Exception
     */
    public Mail getByMessageID(AttacheHandler handler, String messageID) throws Exception {
        this.handler = handler;
        Store connectedStore = getConnectedStore();
        Folder folder = getFolder(connectedStore);
        SearchTerm searchTerm = new MessageIDTerm(messageID);
        Message messages[] = folder.search(searchTerm);
        if (messages == null || messages.length == 0) return null;
        List<Mail> mailList = new ArrayList<Mail>();
        buildMailList(messageID, (MimeMessage) messages[0], mailList);
        return mailList.get(0);
    }

    private Store getConnectedStore() throws MessagingException {
        Session session = getMailSession(mailSetting.getProtocal());
        URLName urln = new URLName(mailSetting.getProtocal(), mailSetting.getReceiveHost(), Integer.parseInt(mailSetting.getReceivePort()),
                null, mailSetting.getMailAddress(), mailSetting.getPassword());
        // 创建连接
        Store store = session.getStore(urln);
        store.connect();
        return store;
    }

    private Folder getFolder(Store store) throws MessagingException {
        Folder folder = store.getFolder("INBOX");
        if (mailSetting.getIsDeleteRemote()) {// 需要删除远程邮件，则以读写方式打开
            folder.open(Folder.READ_WRITE);
        } else {
            folder.open(Folder.READ_ONLY);
        }
        return folder;
    }

    /**
     * 若lastHandleMessageId为空，则返回所有邮件数组：Message[]<br>
     * 若lastHandleMessageId不为空，则会将lastHandleMessageId邮件之后的邮件构造传入的mailList列表，并返回Null
     *
     * @param folder
     * @param lastHandleMessageId
     * @param mailList
     * @return
     * @throws Exception
     */
    private List<Mail> getMessages(Folder folder, String lastHandleMessageId) throws Exception {
        Message messages[] = null;
        FetchProfile profile = new FetchProfile();// 感兴趣的信息
        profile.add(UIDFolder.FetchProfileItem.UID);// 邮件标识id
        folder.fetch(messages, profile);
        int total = folder.getMessageCount();
        List<Mail> mailList = new ArrayList<Mail>();
        boolean isLastHandleMessageIdNotEmpty = (lastHandleMessageId != null && !"".equals(lastHandleMessageId.trim()));
        mailList = getMessages(folder, lastHandleMessageId, mailList, isLastHandleMessageIdNotEmpty, total);

        return mailList;
    }

    /**
     * 若lastHandleMessageId为空，则返回所有邮件数组：Message[]<br>
     * 若lastHandleMessageId不为空，则会将lastHandleMessageId邮件之后的邮件构造传入的mailList列表，并返回Null
     *
     * @param folder
     * @param lastHandleMessageId
     * @param mailList
     * @param isLastHandleMessageIdNotEmpty
     * @param endIndex                      需要获取的最后一封邮件的索引号
     * @return
     * @throws Exception
     */
    private List<Mail> getMessages(Folder folder, String lastHandleMessageId, List<Mail> mailList, boolean isLastHandleMessageIdNotEmpty, int endIndex) throws Exception {
        MimeMessage msg = null;
        int begin = endIndex;
        int end = 1;
        try {
            if (folder instanceof IMAPFolder) {// IMAPFolder获取的message起始索引值是0
                begin = endIndex - 1;
                end = 0;
            }
            for (int i = begin; i >= end; i--) {
                msg = (MimeMessage) folder.getMessage(i);
                String messageId = msg.getMessageID();
                if (isLastHandleMessageIdNotEmpty && messageId.equals(lastHandleMessageId)) break;
                buildMailList(messageId, msg, mailList);
            }
        } catch (FolderClosedException closeException) {// 如果同步的过程中，邮箱连接被关闭了，则重新打开一个连接
            folder = getFolder(folder.getStore());
            getMessages(folder, lastHandleMessageId, mailList, isLastHandleMessageIdNotEmpty, endIndex - mailList.size());
        }

        // 反转list列表，因上述遍历是从最新的邮件开始插入list中，现需要将list进行反转，将最旧的邮件放到list的开头
        // 与folder.getMessages()获取邮件的顺序保持一致
        Collections.reverse(mailList);
        return mailList;
    }

    /**
     * 根据messageId判断是否下载邮件，如果下载，将解析message之后，存放到list对象中
     *
     * @param messageId
     * @param message
     * @param list
     * @throws Exception
     * @see Message
     */
    private void buildMailList(String messageId, MimeMessage message, List<Mail> list) throws Exception {
        if (handler.isDownlad(messageId) == null || !handler.isDownlad(messageId)) return;
        Mail mail = getMail(message);
        mail.setMessageId(messageId);
        list.add(mail);
        if (mailSetting.getIsDeleteRemote() && mailSetting.getIsHandleAttach()) {
            message.setFlag(Flags.Flag.DELETED, true);//设置已删除状态为true
        }
    }

    /**
     * 根据MimeMessage获得Mail实体
     *
     * @param message
     * @return
     * @throws Exception
     * @see    MimeMessage
     */
    private Mail getMail(MimeMessage message) throws Exception {
        Mail mail = new Mail();
        Date sentDate = null;
        if (message.getSentDate() != null) {
            sentDate = message.getSentDate();
        } else {
            sentDate = new Date();
        }
        // 邮件发送时间
        mail.setSendDate(sentDate);
        String subject = message.getSubject();
        if (subject != null) {
            mail.setSubject(MimeUtility.decodeText(subject));
        } else {
            mail.setSubject("无主题");
        }
        // 取得邮件内容
        StringBuffer bodytext = new StringBuffer();
        getMailContent(message, bodytext, mail);
        mail.setContent(bodytext.toString());
        // 发件人
        MailAddress temp = getFrom(message);
        mail.setSenderAddress(temp.getAddress());
        mail.setSenderName(temp.getName());
        // 接受者
        temp = getMailAddress(Message.RecipientType.TO, message);
        mail.setReceiverAddresses(temp.getAddress());
        mail.setReceiverName(temp.getName());
        // 暗送者
        temp = getMailAddress(Message.RecipientType.BCC, message);
        mail.setBcCAddresses(temp.getAddress());
        mail.setBccName(temp.getName());
        // 抄送者
        temp = getMailAddress(Message.RecipientType.CC, message);
        mail.setCopyToAddresses(temp.getAddress());
        mail.setCopyToName(temp.getName());
        return mail;
    }

    /**
     * 获得发件人的地址和姓名
     *
     * @see    MimeMessage
     */
    private MailAddress getFrom(MimeMessage mimeMessage)
            throws Exception {
        MailAddress mailAddress = new MailAddress();
        try {
            InternetAddress address[] = (InternetAddress[]) mimeMessage
                    .getFrom();
            if (address == null || address.length == 0)
                return mailAddress;
            mailAddress.setAddress(address[0].getAddress());
            mailAddress.setName(address[0].getPersonal());
        } catch (Exception ex) {
        }
        return mailAddress;
    }

    /**
     * 根据RecipientType类型，获得邮件相应的收件人信息：邮箱地址,邮箱名称
     *
     * @param recipientType
     * @param mimeMessage
     * @return
     * @throws Exception
     * @see    RecipientType
     * @see    MimeMessage
     */
    private MailAddress getMailAddress(RecipientType recipientType, MimeMessage mimeMessage) throws Exception {
        MailAddress mailAddress = new MailAddress();
        InternetAddress[] address = (InternetAddress[]) mimeMessage.getRecipients(recipientType);
        if (address == null) return mailAddress;
        StringBuffer addresses = new StringBuffer("");
        StringBuffer name = new StringBuffer("");
        for (int i = 0; i < address.length; i++) {
            String email = address[i].getAddress();
            if (email == null) continue;
            String personal = address[i].getPersonal();
            if (personal == null) personal = email;
            switch (i) {
                case 0:
                    addresses.append(MimeUtility.decodeText(email));
                    name.append(MimeUtility.decodeText(personal));
                    break;
                default:
                    addresses.append(",").append(MimeUtility.decodeText(email));
                    name.append(",").append(MimeUtility.decodeText(personal));
            }
        }
        mailAddress.setAddress(addresses.toString());
        mailAddress.setName(name.toString());
        return mailAddress;
    }

    /**
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中， 解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
     *
     * @param    message
     * @param    bodyText
     * @param    mail
     * @see    Part
     * @see    Mail
     */
    private void getMailContent(Part message, StringBuffer bodyText, Mail mail) throws Exception {
        String contentType = message.getContentType();
        int nameindex = contentType.indexOf("name");
        boolean conname = false;
        if (nameindex != -1) {
            conname = true;
        }
        if ((message.isMimeType("text/plain") || message.isMimeType("text/html")) && !conname) {
            bodyText.append((String) message.getContent());
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();

            int count = multipart.getCount();
            Map<String, Part> partMap = new LinkedHashMap<String, Part>();

            boolean blnTxt = false;
            boolean blnHtml = false;
            for (int i = 0; i < count; i++) {
                Part tmpPart = multipart.getBodyPart(i);
                String partType = tmpPart.getContentType();
                if (tmpPart.isMimeType("text/plain")) {
                    partMap.put("text/plain", tmpPart);
                    blnTxt = true;
                } else if (tmpPart.isMimeType("text/html")) {
                    partMap.put("text/html", tmpPart);
                    blnHtml = true;
                } else {
                    partMap.put(partType, tmpPart);
                }
            }
            if (blnTxt && blnHtml) {
                partMap.remove("text/plain");
            }
            Set<Entry<String, Part>> set = partMap.entrySet();
            for (Iterator<Entry<String, Part>> it = set.iterator(); it
                    .hasNext(); ) {
                getMailContent(it.next().getValue(), bodyText, mail);
            }

        } else if (message.isMimeType("message/rfc822")) {
            getMailContent((Part) message.getContent(), bodyText, mail);
        } else if (message.isMimeType("application/octet-stream")
                || message.isMimeType("image/*")
                || message.isMimeType("application/*")) {
            if (mailSetting.getIsHandleAttach()) {
                handler.handle(message, mail);
            } else {
                // 不处理附件下载，则只记录下附件的文件名
                String filename = MimeUtility.decodeText(message.getFileName());
                mail.getMailAttachments().add(new MailAttachment(filename, ""));
            }
        }
    }

    /**
     * 根据传入的协议类型，返回Properties
     *
     * @param protocal 有IMAP、SMTP、POP3
     * @return Properties
     */
    private Properties getProperty(String protocal) {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Properties props = new Properties();
        if (mailSetting.getSSL()) {
            props.setProperty("mail." + protocal + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        props.setProperty("mail." + protocal + ".socketFactory.fallback", "false");

        if (MailSetting.SMTP_PROTOCAL.equals(protocal)) {
            String host = mailSetting.getSendHost();
            props.setProperty("mail.smtp.host", host);
            props.setProperty("mail.smtp.port", mailSetting.getSendPort());
            props.setProperty("mail.smtp.socketFactory.port", mailSetting.getSendPort());
            props.setProperty("mail.smtp.auth", String.valueOf(mailSetting.getValidate()));
            int gmail = host.indexOf("gmail");
            int live = host.indexOf("live");
            if (gmail != -1 || live != -1) {
                props.setProperty("mail.smtp.starttls.enable", "true");
            }
            if (!mailSetting.getSSL()) {
                props.setProperty("mail.smtp.socketFactory.class", "javax.net.SocketFactory");
            }
        } else {
            props.setProperty("mail." + protocal + ".host", mailSetting.getReceiveHost());
            props.setProperty("mail." + protocal + ".port", mailSetting.getReceivePort());
            props.setProperty("mail." + protocal + ".socketFactory.port", mailSetting.getReceivePort());
            if (MailSetting.POP3_PROTOCAL.equals(protocal)) {
                props.setProperty("mail.smtp.starttls.enable", "true");
            } else {
                props.setProperty("mail.store.protocol", MailSetting.IMAP_PROTOCAL);
            }
        }
        return props;
    }

    /**
     * 根据协议，获取邮箱连接session
     *
     * @param protocal 有IMAP、SMTP、POP3
     * @return 邮箱连接session
     */
    private Session getMailSession(String protocal) {
        // Get a Properties object
        Properties props = getProperty(protocal);
        // 如果不要对服务器的ssl证书进行受信任检查，测添加以下语句
        // mailProps.setProperty("mail.smtp.ssl.trust","*");
        Session mailSession = null;
        if (MailSetting.IMAP_PROTOCAL.equals(protocal)) {
            mailSession = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailSetting.getMailAddress(), mailSetting.getPassword());
                }
            });
        } else {
            mailSession = Session.getInstance(props, null);
        }
        return mailSession;
    }

    /**
     * 添加发件人、收件人、抄送人、暗送人地址信息
     *
     * @param mail
     * @param message
     * @throws UnsupportedEncodingException
     * @throws Exception
     * @throws MessagingException
     * @see    Message
     * @see    Mail
     */
    private void addAddressInfo(Mail mail, Message message) throws UnsupportedEncodingException, MessagingException {
        // 添加发件人
        InternetAddress senderAddress = toInternetAddress(mailSetting.getNickName(), mailSetting.getMailAddress());
        message.setFrom(senderAddress);
        // 收件人列表
        addAddressInfo(message, mail.getReceiverAddresses(), Message.RecipientType.TO);
        // 抄送人列表
        addAddressInfo(message, mail.getCopyToAddresses(), Message.RecipientType.CC);
        // 暗送人列表
        addAddressInfo(message, mail.getBcCAddresses(), Message.RecipientType.BCC);
    }

    /**
     * 根据传入的带,号的address，添加地址信息
     *
     * @param mail
     * @param message
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @see    Message
     * @see    RecipientType
     */
    private void addAddressInfo(Message message, String address, RecipientType recipientType) throws UnsupportedEncodingException, MessagingException {
    	if (StringUtil.isEmpty(address)) return ;
    	
        MailAddress mailAddress = new MailAddress();
        List<MailAddress> addressList = new ArrayList<MailAddress>();
        
        String[] addressArr = address.split(",");
        for (String id : addressArr) {
            mailAddress = new MailAddress();
            mailAddress.setAddress(id);
            mailAddress.setName(id);
            addressList.add(mailAddress);
        }
        
        InternetAddress InternetAddressArr[] = toInternetAddress(addressList);
        if (InternetAddressArr != null)
            message.addRecipients(recipientType, InternetAddressArr);
    }

    /**
     * 将地地址址转化为 可输送的网络地址
     *
     * @throws UnsupportedEncodingException
     * @throws AddressException
     * @param    name    显示名称
     * @param    address    邮件地址
     */
    private InternetAddress toInternetAddress(String name, String address)
            throws UnsupportedEncodingException, AddressException {
        if (name != null && !name.trim().equals("")) {
            return new InternetAddress(address, MimeUtility.encodeWord(name,
                    MailUtil.CHARSET, "Q"));
        }
        return new InternetAddress(address);
    }

    /**
     * 将地地址址转化为 可输送的网络地址
     *
     * @throws UnsupportedEncodingException
     * @throws AddressException
     * @param    emailAddress    MailAddress实体对象
     * @see    MailAddress
     */
    private InternetAddress toInternetAddress(MailAddress emailAddress)
            throws UnsupportedEncodingException, AddressException {
        return toInternetAddress(emailAddress.getName(),
                emailAddress.getAddress());
    }

    /**
     * 将地地址址转化为 可输送的网络地址
     *
     * @throws UnsupportedEncodingException
     * @throws AddressException
     * @param    list    MailAddress实体对象列表
     * @see    MailAddress
     */
    private InternetAddress[] toInternetAddress(List<MailAddress> list)
            throws UnsupportedEncodingException, AddressException {
        if (list == null)
            return null;
        InternetAddress address[] = new InternetAddress[list.size()];
        for (int i = 0; i < list.size(); i++) {
            address[i] = toInternetAddress(list.get(i));
        }
        return address;
    }

    /**
     * 关闭邮箱连接，关闭时，根据MailSetting中设置的isDeleteRemote，决定是否删除远程邮件
     *
     * @param folder java.mail.Folder
     * @param store  javax.mail.Store
     * @throws UnsupportedEncodingException
     * @see    Folder
     * @see Store
     */
    private void close(Folder folder, Store store) {
        try {
            if (folder != null && folder.isOpen()) {
                //是否删除远程邮件
                folder.close(mailSetting.getIsDeleteRemote());
            }
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            folder = null;
            store = null;
        }
    }

}
