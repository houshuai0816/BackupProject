package com.andy.service.servierusercenter.controller

import com.andy.andycommonbean.bean.UserBean
import com.andy.andycommonbean.response.BaseResponse
import com.andy.andycommonbean.response.ResultResponse
import com.andy.service.servierusercenter.bean.RegisterInfoBean
import com.andy.service.servierusercenter.service.IUserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * describe: 用户操作的内容
 * author 候帅
 * date 2018/8/30 下午2:52
 */
@RestController
@RequestMapping(value = ["/admin/user"])
class UserController {

    private val log: Logger = LoggerFactory.getLogger(UserController::class.java)


    @Autowired
    private lateinit var iUserService: IUserService

    @PostMapping("/register")
    fun registerUser(@RequestBody registerInfoBean: RegisterInfoBean): BaseResponse {
        val registerByAccount = iUserService.registerByAccount(registerInfoBean)
        return ResultResponse.Success(registerByAccount)
    }

    @GetMapping("/find")
    fun findUserInfo(@RequestParam(value = "account") accout: String): Optional<UserBean> {
        return Optional.ofNullable(iUserService.findUserInfo(accout))
    }




}