package com.andy.service.servierusercenter.bean


/**
 * describe: 组相关的描述内容
 * author 候帅
 * date 2018/9/19 上午9:37
 */
class GroupBean {

    var groupName: String? = null

    var groupId: String? = null

    var groupIcon: String? = null

    var groupLevel: Int = -1

    var children: List<GroupBean>? = null
}