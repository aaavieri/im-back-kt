package com.wwx.imback.dao

import com.wwx.imback.dto.table.UserTokenDto
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Update

@Mapper
interface UserTokenMapper {

    @Insert("insert into t_user_token (server_user_id, token, login_time, expire_time) values" +
            " (#{serverUserId}, #{token}, sysdate(), #{expireTime})" +
            " on duplicate key update token = #{token}, login_time = sysdate(), expire_time = #{expireTime}," +
            " update_time = sysdate(), del_flag = 0, row_version = row_version + 1")
    fun saveToken(userTokenDto: UserTokenDto): Int

    @Update("update t_user_token set del_flag = 1, row_version = row_version + 1" +
            " where server_user_id = #{serverUserId} and del_flag = 0")
    fun deleteToken(@Param("serverUserId") serverUserId: Int): Int
}