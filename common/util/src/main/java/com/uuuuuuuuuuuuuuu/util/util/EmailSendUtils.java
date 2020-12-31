package com.uuuuuuuuuuuuuuu.util.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;


public class EmailSendUtils {
	
	private static Logger logger = LoggerFactory.getLogger(EmailSendUtils.class);
	
	public static final String PREFIX = "email"; 
	
	static String hostServer = "smtp.exmail.qq.com";
	
	static String title = "一方决策系统通知";
	
	static String account = "report@paohe.cn";
	
	static String passwd = "Phw1234";
	
	static String content = "您好！您正在申请修改密码！此次验证码为code，请妥善保管，有效时间为5分钟。";
	
//	public static void main(String[] args) {
//		EmailSendUtils.emailSend("2466845324@qq.com","9527");
//	}
	
	public static boolean emailSend(String email,String code) {
		String msg = content.replace("code", code);
		return emailSend(email, hostServer, account, passwd, title, msg, null);
	}
	
	/**TODO 邮件发送
	 * @param email 收件箱数组，可发送多个
	 * @param hostServer 服务器地址，如:腾讯企业邮箱为：smtp.exmail.qq.com，QQ邮箱服务地址：smtp.qq.com必填
	 * @param account 发件人邮箱 ，必填
	 * @param passwd  发件人邮箱密码，必填
	 * @param title   邮件标题 ，必填
	 * @param content 邮件内容，必填
	 * @param from  发件人描述,非必填
	 * @return boolean 是否发送成功
	 * @author:    吕观林
	 * @date:      2018年5月25日 上午10:09:32 
	 * @throws   
	 */ 
	private static boolean emailSend(String email, String hostServer, String account, String passwd, 
			String title, String content, String from){
		try{
			//开始发送邮件
			if(StringUtils.isBlank(account) || StringUtils.isBlank(passwd) || StringUtils.isBlank(hostServer)){
				return false;
			}
			HtmlEmail emailHtml = new HtmlEmail();
			//收件箱
			List<InternetAddress> addressList = new ArrayList<InternetAddress>();
			InternetAddress address = new InternetAddress(email, email, null);
			addressList.add(address);
			emailHtml.setHostName(hostServer);
			emailHtml.setAuthentication(account, passwd);
			if(!StringUtils.isBlank(from)){
				emailHtml.setFrom(account, from);
			}else{
				emailHtml.setFrom(account);
			}
			emailHtml.setSmtpPort(465);
			emailHtml.setCharset("UTF-8");
			emailHtml.setSubject(title); // 标题
			emailHtml.setHtmlMsg(content);
			emailHtml.setTo(addressList);
			emailHtml.setSSLOnConnect(true);
			emailHtml.send();
//			System.out.println("邮件发送成功"+"email[{"+email+"}]");
            logger.info("邮件发送成功,  email[{}]",email);
            return true;
		}catch (Exception e) {
//			System.out.println("邮件发送出错[{"+e.getMessage()+"}]");
			logger.debug("邮件发送出错[{}]",e.getMessage());
			return false;
		}
	}
}
