package com.dstz.sys.email.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 邮件实体类
 * </pre>
 */
public class Mail implements Serializable {

    private static final long serialVersionUID = 4311266253309771066L;
    /**
     * 每种邮箱中，邮件的唯一ID
     */
    protected String messageId;
    /**
     * 发件人显示名
     */
    protected String senderName;
    /**
     * 发件人地址
     */
    protected String senderAddress;
    /**
     * 邮件主题
     */
    protected String subject;
    /**
     * 邮件内容
     */
    protected String content;
    /**
     * 发送时间
     */
    protected Date sendDate;
    /**
     * 收件人显示名
     */
    protected String receiverName;
    /**
     * 收件人地址
     */
    protected String receiverAddresses;
    /**
     * 抄送人显示名
     */
    protected String copyToName;
    /**
     * 抄送人地址
     */
    protected String copyToAddresses;
    /**
     * 暗送人显示名
     */
    protected String bccName;
    /**
     * 暗送人地址
     */
    protected String bcCAddresses;
    /**
     * 邮件附件
     */
    protected List<MailAttachment> attachments = new ArrayList<MailAttachment>();

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBcCAddresses() {
        return bcCAddresses;
    }

    public void setBcCAddresses(String bcCAddresses) {
        this.bcCAddresses = bcCAddresses;
    }

    public List<MailAttachment> getMailAttachments() {
        return attachments;
    }

    public void setMailAttachments(List<MailAttachment> attachments) {
        this.attachments = attachments;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCopyToName() {
        return copyToName;
    }

    public void setCopyToName(String copyToName) {
        this.copyToName = copyToName;
    }

    public String getBccName() {
        return bccName;
    }

    public void setBccName(String bccName) {
        this.bccName = bccName;
    }

    public String getCopyToAddresses() {
        return copyToAddresses;
    }

    public void setCopyToAddresses(String copyToAddresses) {
        this.copyToAddresses = copyToAddresses;
    }

    public String getReceiverAddresses() {
        return receiverAddresses;
    }

    public void setReceiverAddresses(String receiverAddresses) {
        this.receiverAddresses = receiverAddresses;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result
                + ((bcCAddresses == null) ? 0 : bcCAddresses.hashCode());
        result = prime * result + ((bccName == null) ? 0 : bccName.hashCode());
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result
                + ((copyToAddresses == null) ? 0 : copyToAddresses.hashCode());
        result = prime * result
                + ((copyToName == null) ? 0 : copyToName.hashCode());
        result = prime * result + ((attachments == null) ? 0 : attachments.hashCode());
        result = prime
                * result
                + ((receiverAddresses == null) ? 0 : receiverAddresses
                .hashCode());
        result = prime * result
                + ((receiverName == null) ? 0 : receiverName.hashCode());
        result = prime * result
                + ((sendDate == null) ? 0 : sendDate.hashCode());
        result = prime * result
                + ((senderAddress == null) ? 0 : senderAddress.hashCode());
        result = prime * result
                + ((senderName == null) ? 0 : senderName.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Mail [messageId=" + messageId + ", senderName=" + senderName
                + ", senderAddress=" + senderAddress + ", subject=" + subject
                + ", content=" + content + ", sendDate=" + sendDate
                + ", receiverName=" + receiverName + ", receiverAddresses="
                + receiverAddresses + ", copyToName=" + copyToName
                + ", copyToAddresses=" + copyToAddresses + ", bccName="
                + bccName + ", bcCAddresses=" + bcCAddresses + ", attachments="
                + attachments + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Mail))
            return false;
        Mail other = (Mail) obj;
        if (messageId == null) {
            if (other.messageId != null)
                return false;
        } else if (!messageId.equals(other.messageId))
            return false;
        if (bcCAddresses == null) {
            if (other.bcCAddresses != null)
                return false;
        } else if (!bcCAddresses.equals(other.bcCAddresses))
            return false;
        if (bccName == null) {
            if (other.bccName != null)
                return false;
        } else if (!bccName.equals(other.bccName))
            return false;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (copyToAddresses == null) {
            if (other.copyToAddresses != null)
                return false;
        } else if (!copyToAddresses.equals(other.copyToAddresses))
            return false;
        if (copyToName == null) {
            if (other.copyToName != null)
                return false;
        } else if (!copyToName.equals(other.copyToName))
            return false;
        if (attachments == null) {
            if (other.attachments != null)
                return false;
        } else if (!attachments.equals(other.attachments))
            return false;
        if (receiverAddresses == null) {
            if (other.receiverAddresses != null)
                return false;
        } else if (!receiverAddresses.equals(other.receiverAddresses))
            return false;
        if (receiverName == null) {
            if (other.receiverName != null)
                return false;
        } else if (!receiverName.equals(other.receiverName))
            return false;
        if (sendDate == null) {
            if (other.sendDate != null)
                return false;
        } else if (!sendDate.equals(other.sendDate))
            return false;
        if (senderAddress == null) {
            if (other.senderAddress != null)
                return false;
        } else if (!senderAddress.equals(other.senderAddress))
            return false;
        if (senderName == null) {
            if (other.senderName != null)
                return false;
        } else if (!senderName.equals(other.senderName))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        return true;
    }


}