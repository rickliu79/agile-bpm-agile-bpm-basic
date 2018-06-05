package com.dstz.sys.email.model;

import java.io.Serializable;

/**
 * <pre>
 * 描述：邮箱设置实体类
 * </pre>
 */
public class MailSetting implements Serializable {
    protected static final long serialVersionUID = -1563590072023033989L;
    // 邮件服务器的IP、端口
    protected String sendHost;
    protected String sendPort;

    // 邮件接收服务器的IP、端口和协议
    protected String receiveHost;
    protected String receivePort;
    protected String protocal;

    // 是否是SSL
    protected Boolean SSL = false;

    // 是否需要身份验证
    protected Boolean validate = true;

    // 登陆邮件服务器的用户名和密码
    protected String mailAddress;
    protected String password;

    // 用户昵称
    protected String nickName;

    // 是否收取附件
    protected Boolean isHandleAttach = true;

    // 是否删除服务器上的邮件
    protected Boolean isDeleteRemote = false;

    public final static String SMTP_PROTOCAL = "smtp";
    public final static String POP3_PROTOCAL = "pop3";
    public final static String IMAP_PROTOCAL = "imap";

    public Boolean getSSL() {
        return SSL;
    }

    public void setSSL(Boolean SSL) {
        this.SSL = SSL;
    }

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getReceiveHost() {
        return receiveHost;
    }

    public void setReceiveHost(String receiveHost) {
        this.receiveHost = receiveHost;
    }

    public String getReceivePort() {
        return receivePort;
    }

    public void setReceivePort(String receivePort) {
        this.receivePort = receivePort;
    }

    public Boolean getIsHandleAttach() {
        return isHandleAttach;
    }

    public void setIsHandleAttach(Boolean isHandleAttach) {
        this.isHandleAttach = isHandleAttach;
    }

    public String getSendHost() {
        return sendHost;
    }

    public void setSendHost(String sendHost) {
        this.sendHost = sendHost;
    }

    public String getSendPort() {
        return sendPort;
    }

    public void setSendPort(String sendPort) {
        this.sendPort = sendPort;
    }

    public Boolean getIsDeleteRemote() {
        return isDeleteRemote;
    }

    public void setIsDeleteRemote(Boolean isDeleteRemote) {
        this.isDeleteRemote = isDeleteRemote;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((SSL == null) ? 0 : SSL.hashCode());
        result = prime * result
                + ((isDeleteRemote == null) ? 0 : isDeleteRemote.hashCode());
        result = prime * result
                + ((isHandleAttach == null) ? 0 : isHandleAttach.hashCode());
        result = prime * result
                + ((mailAddress == null) ? 0 : mailAddress.hashCode());
        result = prime * result
                + ((nickName == null) ? 0 : nickName.hashCode());
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result
                + ((protocal == null) ? 0 : protocal.hashCode());
        result = prime * result
                + ((receiveHost == null) ? 0 : receiveHost.hashCode());
        result = prime * result
                + ((receivePort == null) ? 0 : receivePort.hashCode());
        result = prime * result
                + ((sendHost == null) ? 0 : sendHost.hashCode());
        result = prime * result
                + ((sendPort == null) ? 0 : sendPort.hashCode());
        result = prime * result
                + ((validate == null) ? 0 : validate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof MailSetting))
            return false;
        MailSetting other = (MailSetting) obj;
        if (SSL == null) {
            if (other.SSL != null)
                return false;
        } else if (!SSL.equals(other.SSL))
            return false;
        if (isDeleteRemote == null) {
            if (other.isDeleteRemote != null)
                return false;
        } else if (!isDeleteRemote.equals(other.isDeleteRemote))
            return false;
        if (isHandleAttach == null) {
            if (other.isHandleAttach != null)
                return false;
        } else if (!isHandleAttach.equals(other.isHandleAttach))
            return false;
        if (mailAddress == null) {
            if (other.mailAddress != null)
                return false;
        } else if (!mailAddress.equals(other.mailAddress))
            return false;
        if (nickName == null) {
            if (other.nickName != null)
                return false;
        } else if (!nickName.equals(other.nickName))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (protocal == null) {
            if (other.protocal != null)
                return false;
        } else if (!protocal.equals(other.protocal))
            return false;
        if (receiveHost == null) {
            if (other.receiveHost != null)
                return false;
        } else if (!receiveHost.equals(other.receiveHost))
            return false;
        if (receivePort == null) {
            if (other.receivePort != null)
                return false;
        } else if (!receivePort.equals(other.receivePort))
            return false;
        if (sendHost == null) {
            if (other.sendHost != null)
                return false;
        } else if (!sendHost.equals(other.sendHost))
            return false;
        if (sendPort == null) {
            if (other.sendPort != null)
                return false;
        } else if (!sendPort.equals(other.sendPort))
            return false;
        if (validate == null) {
            if (other.validate != null)
                return false;
        } else if (!validate.equals(other.validate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MailSeting [sendHost=" + sendHost + ", sendPort=" + sendPort
                + ", receiveHost=" + receiveHost + ", receivePort="
                + receivePort + ", protocal=" + protocal + ", SSL=" + SSL
                + ", validate=" + validate + ", mailAddress=" + mailAddress
                + ", password=" + password + ", nickName=" + nickName
                + ", isHandleAttach=" + isHandleAttach + ", isDeleteRemote="
                + isDeleteRemote + "]";
    }
}