package com.wwx.imback.dao

import com.wwx.imback.dto.table.ClientInfoDto
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface ClientInfoMapper {

    @Select(value = ["<script>",
        "select open_id, user_name, avatar, phone_num, user_status from t_client_info where open_id ",
        "in ('-1' ",
        "<foreach collection='openIdList' item='openId' open=',' separator=',' close=''>",
        "#{openId}",
        "</foreach>",
        ") and del_flag = 0 ",
        "</script>"])
    fun getClientList(@Param("openIdList")openIdList: List<String>): List<ClientInfoDto>

    @Select("select open_id, user_name, avatar, phone_num, user_status from t_client_info where open_id = #{openId} and del_flag = 0")
    fun getClientByOpenId(@Param("openId")openId: String): ClientInfoDto?
}