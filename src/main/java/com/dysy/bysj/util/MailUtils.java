package com.dysy.bysj.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author: Dai Junfeng
 * @create: 2021-01-28
 **/
@Component
public class MailUtils {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 发送纯文本邮件
     * @param subject 邮件主题
     * @param text 邮件正文
     * @param receiver 接收方
     */
    public void sendTextMail(String subject, String text, String... receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        // 可传递多个（即群发）
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
            System.out.println("已发送");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送 html 文本邮件
     * @param subject 邮件主题
     * @param htmlText html文本
     * @param receiver 接收方
     */
    public void sendHtmlMail(String subject, String htmlText, String... receiver) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            // 可以传递数组（即群发）
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(htmlText, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送带附件的邮件
     * @param subject 邮件主题
     * @param htmlText html文本
     * @param receiver 接收方
     */
    public void sendMultipartMail(String subject, String htmlText, String... receiver) {
        String filePath="./file/P35.jpg";
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(htmlText, true);
            // 以文件系统的绝对路径的方式访问静态资源
            FileSystemResource file = new FileSystemResource(new File(filePath));
            // 获取文件名
            String fileName = filePath.substring(filePath.lastIndexOf("/"),filePath.lastIndexOf("."));
            // 添加附件（文件）
            helper.addAttachment(fileName, file);

            javaMailSender.send(message);
            System.out.println("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片邮件
     * @param subject 邮件主题
     * @param receiver 接收方
     */
    public void sendImageMail(String subject, String... receiver) {
        // Id 任意
        String Id = "image";
        String content="<html><body>这是有图片的邮件：<img src=\'cid:" + Id + "\' ></body></html>";
        String imgPath = "./file/P35.jpg";
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource res = new FileSystemResource(new File(imgPath));
            // 将图片放到 Id处
            helper.addInline(Id, res);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
