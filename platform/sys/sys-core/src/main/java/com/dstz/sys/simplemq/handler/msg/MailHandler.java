package com.dstz.sys.simplemq.handler.msg;

import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dstz.base.core.util.PropertyUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.jms.model.msg.NotifyMessage;
import com.dstz.sys.api.service.SysIdentityConvert;
import com.dstz.sys.util.EmailUtil;

/**
 * 邮件消息处理器。
 */
@Component
public class MailHandler extends AbsNotifyMessageHandler<NotifyMessage> {
	@Resource
	SysIdentityConvert sysIdentityConvert;
	
    @Override
    public String getType() {
        return "email";
    }


    @Override
    public String getTitle() {
        return "邮件";
    }


    @Override
    public boolean getSupportHtml() {
        return true;
    }

	@Override
	public boolean sendMessage(NotifyMessage notifMessage) {
		String fromEmail = PropertyUtil.getProperty("mail.address");
	  
        List<IUser> recievers = sysIdentityConvert.convert2Users(notifMessage.getReceivers());
        
        for (IUser reciver : recievers) {
            String email = reciver.getEmail();
            if (StringUtil.isEmpty(email)) continue;
            try {
                EmailUtil.sendEmail(email, "", "", fromEmail, notifMessage.getSubject(), notifMessage.getHtmlContent());
            } catch (MessagingException e) {
            	LOG.error(JSON.toJSONString(notifMessage));
            	LOG.error("发送邮件失败！",e);
            }
        }
        LOG.debug("发送邮件成功 ：{}",JSON.toJSONString(notifMessage));
        return true;
	}

}
