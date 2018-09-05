package com.andy.service.servierusercenter.controller

import com.andy.andycommonbean.response.BaseResponse
import com.andy.andycommonbean.response.ResultResponse
import com.andy.andycommonutils.RandomUtil
import com.andy.service.servierusercenter.entity.GroupEntity
import com.andy.service.servierusercenter.service.IGroupService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*

/**
 * describe: 组信息管理
 * author 候帅
 * date 2018/9/2 下午10:27
 */
@RestController
@RequestMapping(value = ["/admin/group"])
class GroupController {

    private val log: Logger = LoggerFactory.getLogger(GroupController::class.java)

    @Autowired
    private lateinit var iGroupService: IGroupService

    /**
     * describe: 组相关操作
     * author 候帅
     * date 2018/9/5 下午10:05
     * @param groupName 组名
     * @param parentId 上级组id
     * @param des 描述内容
     * @return 响应内容
     */
    @PostMapping(value = ["/add"])
    fun addGroup(@RequestParam("groupName") groupName: String,
                 @RequestParam("parentId") parentId: String,
                 @RequestParam("des") des: String): BaseResponse {
        val groupEntity = GroupEntity()
        groupEntity.parentId = parentId
        groupEntity.groupName = groupName
        groupEntity.groupDesc = des
        groupEntity.groupId = RandomUtil.generteRandomUUID()
        iGroupService.addGroup(groupEntity)
        return ResultResponse.Success()
    }


    /**
     * describe: 组的删除
     * author 候帅
     * date 2018/9/5 下午10:22
     * @param groupId 组ID内容
     * @return   响应结果
     */
    @DeleteMapping(value = ["/delete/{groupId}"])
    fun deleteGroup(@PathVariable(value = "groupId") groupId: String): BaseResponse {
        iGroupService.deleteGroupById(groupId)
        return ResultResponse.Success()
    }

    /**
     * describe: 修改组信息
     * author 候帅
     * date 2018/9/5 下午10:25
     * @param
     * @return
     */
    @PutMapping(value = ["/modify/info"])
    fun modiftyGroupInfo(des: String, groupName: String, groupId: String): BaseResponse {
        val groupEntity = iGroupService.findGroupInfoByGroupId(groupId)
        return groupEntity.map {
            if (!StringUtils.isEmpty(groupName))
                it.groupName = groupName
            if (!StringUtils.isEmpty(des))
                it.groupDesc = des
            iGroupService.modiftyGroupInfo(it)
            return@map ResultResponse.Success()
        }.orElse(ResultResponse.Error())

    }

}