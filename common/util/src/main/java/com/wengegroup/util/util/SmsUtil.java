package com.wengegroup.util.util;


import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import com.wengegroup.util.exception.SmsSendFailException;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.stream.Stream;

//导入可选配置类
@Slf4j
public class SmsUtil {

	// 短信应用 SDK AppID
	private static final Integer appid = 1400392535; // SDK AppID 以1400开头
	// 短信应用 SDK AppKey
	private static final String appkey = "3f8c82ac67bd4c6936021978af7e18e0";

	private static final String secretId = "AKIDoPlLglQ9VbljW6jwFKCn8KI0RfdKlFBr";

	private static final String secretKey = "zjDRYOHmBS5VkofLqBG8k70kDTVIq9JN";
	// 需要发送短信的手机号码
//	public static final String[] phoneNumbers = { "+8613149383022"};
	// 短信模板 ID，需要在短信应用中申请
	private static final Integer templateId = 655084; // NOTE: 这里的模板 ID`7839`只是示例，真实的模板 ID 需要在短信控制台中申请
	// 签名
	private static final String smsSign = "量芯"; // NOTE: 签名参数使用的是`签名内容`，而不是`签名ID`。这里的签名"腾讯云"只是示例，真实的签名需要在短信控制台申请

	/**
	 *
	 * @param desc 业务场景  申请创立Mdtsk多维时空专属节点，重置Mdtsk多位时空专属节点密码，等
	 * @param phoneNumber 手机号码(国内)
	 * @param code 验证码
	 * @param format  时间格式  秒/分钟/天/小时
	 * @return 验证码
	 */
	public static void sendCode(String desc,String phoneNumber,String code,String expire,String format) {

		try {
			SendSmsRequest req = assembleSmsRequest(phoneNumber);

			/* 模板参数: 若无模板参数，则设置为空 */

			String[] templateParams = { desc,code,expire,format};
			req.setTemplateParamSet(templateParams);
			SmsClient client = smsClient();
			/*
			 * 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的 返回的 res 是一个 SendSmsResponse
			 * 类的实例，与请求对象对应
			 */
			SendSmsResponse res = client.SendSms(req);

			SendStatus[] sendStatusSet = res.getSendStatusSet();

			if (sendStatusSet.length==0) {
				throw new SmsSendFailException("短信验证码发送失败");
			}
			if (Stream.of(sendStatusSet).anyMatch(sendStatus -> sendStatus.getFee()==0)) {
				log.error(SendSmsResponse.toJsonString(res));
				throw new SmsSendFailException("短信验证码发送失败");
			}
		} catch (TencentCloudSDKException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送短信
	 * @param phoneNumber 电话号码
	 * @param code 验证码 (5位)
	 */
	public static void sendCodeByType(String phoneNumber,String code,String type) {
		if (type == null || "1".equals(type)){
			sendCodeWhenCreate(phoneNumber,code);
		}else if ("2".equals(type)){
			sendCodeWhenResetPwd(phoneNumber,code);
		}else if ("3".equals(type)){
			sendCodeWhenLogin(phoneNumber,code);
		}
	}

	/**
	 * 创立节点发送短信
	 * @param phoneNumber 电话号码
	 * @param code 验证码 (5位)
	 */
	public static void sendCodeWhenCreate(String phoneNumber,String code) {
		sendCode("申请创立Mdtsk多维时空专属节点",phoneNumber,code,"120","秒");
	}

	/**
	 * 登录Mdtsk多维时空专属节点
	 * @param phoneNumber 电话号码
	 * @param code 验证码
	 */
	public static void sendCodeWhenLogin(String phoneNumber,String code) {
		sendCode("登录Mdtsk多维时空专属节点",phoneNumber,code,"120","秒");
	}

	/**
	 * 重置密码发送短信
	 * @param phoneNumber 电话号码
	 * @param code 验证码
	 */
	public static void sendCodeWhenResetPwd(String phoneNumber,String code) {
		sendCode("重置Mdtsk多维时空专属节点密码",phoneNumber,code,"120","秒");
	}

	/**
	 * 创立节点默认5位验证码
	 * @param phoneNumber 手机号
	 */
	public static void sendCodeWhenCreateWithDefaultCode(String phoneNumber) {
		sendCode("申请创立Mdtsk多维时空专属节点",phoneNumber,getStringRandom(5),"1","分钟");
	}

	/**
	 * 重置密码默认5位验证码
	 * @param phoneNumber 手机号码
	 */
	public static void sendCodeWhenResetPwdWithDefaultCode(String phoneNumber) {
		sendCode("重置Mdtsk多位时空专属节点密码",phoneNumber,getStringRandom(5),"1","分钟");
	}



	public static String getStringRandom(int length) {

		String val = "";
		Random random = new Random();
		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	public static SmsClient smsClient() {
		Credential cred = new Credential(secretId, secretKey);

		// 实例化一个 http 选项，可选，无特殊需求时可以跳过
		HttpProfile httpProfile = new HttpProfile();
		// 设置代理
//	            httpProfile.setProxyHost("host");
//	            httpProfile.setProxyPort(port);
		/*
		 * SDK 默认使用 POST 方法。 如需使用 GET 方法，可以在此处设置，但 GET 方法无法处理较大的请求
		 */
		httpProfile.setReqMethod("POST");
		/*
		 * SDK 有默认的超时时间，非必要请不要进行调整 如有需要请在代码中查阅以获取最新的默认值
		 */
		httpProfile.setConnTimeout(60);
		/*
		 * SDK 会自动指定域名，通常无需指定域名，但访问金融区的服务时必须手动指定域名 例如 SMS 的上海金融区域名为
		 * phone.ap-shanghai-fsi.tencentcloudapi.com
		 */
		httpProfile.setEndpoint("sms.tencentcloudapi.com");

		/*
		 * 非必要步骤: 实例化一个客户端配置对象，可以指定超时时间等配置
		 */
		ClientProfile clientProfile = new ClientProfile();
		/*
		 * SDK 默认用 TC3-HMAC-SHA256 进行签名 非必要请不要修改该字段
		 */
		clientProfile.setSignMethod("HmacSHA256");
		clientProfile.setHttpProfile(httpProfile);
		/*
		 * 实例化 SMS 的 client 对象 第二个参数是地域信息，可以直接填写字符串 ap-guangzhou，或者引用预设的常量
		 */
		return new SmsClient(cred, "", clientProfile);
	}

	private static SendSmsRequest assembleSmsRequest(String phoneNumber) {
		/*
		 * 必要步骤： 实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId 和 secretKey
		 * 本示例采用从环境变量读取的方式，需要预先在环境变量中设置这两个值 您也可以直接在代码中写入密钥对，但需谨防泄露，不要将代码复制、上传或者分享给他人 CAM
		 * 密钥查询：https://console.cloud.tencent.com/cam/capi
		 */
		SmsClient client = smsClient();
		/*
		 * 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数 您可以直接查询 SDK 源码确定接口有哪些属性可以设置
		 * 属性可能是基本类型，也可能引用了另一个数据结构 推荐使用 IDE 进行开发，可以方便地跳转查阅各个接口和数据结构的文档说明
		 */
		SendSmsRequest req = new SendSmsRequest();

		/*
		 * 填充请求参数，这里 request 对象的成员变量即对应接口的入参 您可以通过官网接口文档或跳转到 request 对象的定义处查看请求参数的定义
		 * 基本类型的设置: 帮助链接： 短信控制台：https://console.cloud.tencent.com/smsv2 phone
		 * helper：https://cloud.tencent.com/document/product/382/3773
		 */

		/* 短信应用 ID: 在 [短信控制台] 添加应用后生成的实际 SDKAppID，例如1400006666 */
		req.setSmsSdkAppid(appid.toString());

		/* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，可登录 [短信控制台] 查看签名信息 */
//			String sign = "签名内容";
		req.setSign(smsSign);

//	            /* 国际/港澳台短信 senderid: 国内短信填空，默认未开通，如需开通请联系 [phone helper] */
//	            String senderid = "xxx";
//	            req.setSenderId(senderid);
//
//	            /* 用户的 session 内容: 可以携带用户侧 ID 等上下文信息，server 会原样返回 */
//	            String session = "xxx";
//	            req.setSessionContext(session);
//
//	            /* 短信码号扩展号: 默认未开通，如需开通请联系 [phone helper] */
//	            String extendcode = "xxx";
//	            req.setExtendCode(extendcode);

		/* 模板 ID: 必须填写已审核通过的模板 ID，可登录 [短信控制台] 查看模板 ID */
		req.setTemplateID(templateId.toString());

		/*
		 * 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号] 例如+8613711112222， 其中前面有一个+号
		 * ，86为国家码，13711112222为手机号，最多不要超过200个手机号
		 */
		String[] phoneSet = { phoneNumber };
		req.setPhoneNumberSet(phoneSet);
		return req;
	}
}
