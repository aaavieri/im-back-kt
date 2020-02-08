package com.wwx.imback.dao

import com.wwx.imback.dto.table.UserDto
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface UserMapper {

    @Select("select server_user_id, server_user_account, channel_id, server_user_pass, server_user_name, settings from t_user" +
            " where server_user_account = #{serverUserAccount} and del_flag = 0 limit 1")
    fun getUserByAccount(@Param("serverUserAccount") serverUserAccount: String): UserDto?

    @Select("select server_user_id, server_user_name from t_user where server_user_id = #{serverUserId} and del_flag = 0")
    fun getUserById(@Param("serverUserId") serverUserId: Int): UserDto?

    @Update("update t_user set server_user_pass = #{password}, update_time = sysdate(), row_version = row_version + 1 " +
            "where server_user_id = #{serverUserId} and del_flag = 0")
    fun changePassword(@Param("serverUserId") serverUserId: Int, @Param("password") password: String): Int

    @Select("select u.server_user_id, u.server_user_name from t_user u inner join t_user_token t" +
            " on (u.server_user_id = t.server_user_id) where u.server_user_id = #{serverUserId} and u.channel_id = #{channelId}" +
            " and t.del_flag = 0 and u.del_flag = 0 and t.expire_time > sysdate()")
    fun getLoginUserById(@Param("serverUserId") serverUserId: Int, @Param("channelId")channelId: Int): UserDto?

    @Select("select u.server_user_id, u.server_user_name from t_user u inner join t_user_token t" +
            " on (u.server_user_id = t.server_user_id) where u.channel_id = #{channelId} and t.del_flag = 0" +
            " and u.del_flag = 0 and t.expire_time > sysdate()")
    fun getLoginUserList(@Param("channelId")channelId: Int): List<UserDto>
}