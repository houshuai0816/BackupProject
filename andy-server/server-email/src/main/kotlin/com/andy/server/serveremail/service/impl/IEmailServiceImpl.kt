package com.andy.server.serveremail.service.impl

import com.andy.andycommonbean.bean.EmailBean
import com.andy.andycommonutils.RandomUtil
import com.andy.server.serveremail.dao.EmailDao
import com.andy.server.serveremail.entity.EmailEntity

import com.andy.server.serveremail.service.IEmailService
import org.bouncycastle.asn1.iana.IANAObjectIdentifiers.mail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMailMessage
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class IEmailServiceImpl: IEmailService {


    private val log: Logger = LoggerFactory.getLogger(IEmailServiceImpl::class.java)

    @Autowired
    private lateinit var jms: JavaMailSender

    @Autowired
    private lateinit var emailDao: EmailDao

    @Value("\${email.activeTime}")
    private lateinit var ACTIVETIME: String

    @Transactional
    override fun sendEmtail(emailBean: EmailBean): EmailBean {
        // 富文本方式发送短信 内容 TODO 短信内容的定制
        val msg = jms.createMimeMessage()
        val helper = MimeMessageHelper(msg, true)
        helper.setFrom(emailBean.sender)
        helper.setTo(emailBean.receiver)
        helper.setSubject(emailBean.subject)
        helper.setText(emailBean.text, true)
        jms.send(msg)
        return emailBean.run {
            val emailEntity = EmailEntity()
            emailEntity.sender = emailBean.sender
            emailEntity.receiver = emailBean.receiver
            emailEntity.subject = emailBean.subject
            emailEntity.text = emailBean.text
            val save = emailDao.save(emailEntity)
            this.eid = save.id.toString()
            return@run this
        }
    }

    override fun emailValid(code: String, email: String): Boolean {
        val emailEntity = emailDao.findFirstByReceiverOrderByCreatedDate(email)
        return emailEntity.map {
            val now = ZonedDateTime.now()
            val createdDate = it.createdDate
            if (createdDate.year == now.year
                    && createdDate.dayOfYear == now.dayOfYear
                    && createdDate.hour == now.hour
                    && (createdDate.minute - now.minute) > ACTIVETIME.toInt())
                return@map false
            return@map true
        }.orElse(false)
    }

    @Transactional
    override fun sendEmailByIdentifyCode(emailBean: EmailBean): Boolean {
        // 进行简易 邮件发送
        emailBean.text =  RandomUtil.generteSixNumber().toString()
        val mail = SimpleMailMessage()
        mail.setFrom(emailBean.sender)
        mail.setTo(emailBean.receiver)
        mail.setSubject(emailBean.subject)
        mail.setText(emailBean.text)
        jms.send(mail)
        return emailBean.run {
            val emailEntity = EmailEntity()
            emailEntity.sender = emailBean.sender
            emailEntity.receiver = emailBean.receiver
            emailEntity.subject = emailBean.subject
            emailEntity.text = emailBean.text
            val save = emailDao.save(emailEntity)
            this.eid = save.id.toString()
            return@run true
        }.or(false)
    }
}