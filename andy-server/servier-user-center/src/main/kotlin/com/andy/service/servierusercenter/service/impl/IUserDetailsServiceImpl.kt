package com.andy.service.servierusercenter.service.impl

import com.andy.service.servierusercenter.dao.UserDetailsrDao
import com.andy.service.servierusercenter.entity.UserDetailsEntity
import com.andy.service.servierusercenter.service.IUserDetailsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class IUserDetailsServiceImpl: IUserDetailsService {

    private val log: Logger = LoggerFactory.getLogger(IUserDetailsServiceImpl::class.java)

    @Autowired
    private lateinit var userDetailsrDao: UserDetailsrDao

    override fun save(userDetails: UserDetailsEntity): Optional<UserDetailsEntity> {
       return Optional.ofNullable(userDetailsrDao.save(userDetails))
    }

}