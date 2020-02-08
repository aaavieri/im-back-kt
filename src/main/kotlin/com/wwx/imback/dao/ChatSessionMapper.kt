package com.wwx.imback.dao

import com.wwx.imback.dto.table.ChatSessionDto
import org.apache.ibatis.annotations.*
import java.util.*

@Mapper
interface ChatSessionMapper {

    @Select("select session_id, open_id, start_time, end_time from t_chat_session " +
            " where server_user_id = #{serverUserId} and del_flag = 0 order by session_id desc limit 10")
    fun getUserSessionList(@Param("serverUserId")serverUserId: Int): List<ChatSessionDto>

    @Select("select server_user_id, open_id, session_id, start_time, end_time, message_count, last_access_time," +
            " rank from t_chat_session where session_id = #{sessionId} and del_flag = 0")
    fun getSessionById(@Param("sessionId")sessionId: Int): ChatSessionDto?

    @Select(value = ["<script>",
        "select session_id, server_user_id, open_id from t_chat_session ",
        "where server_user_id in (-1, ",
        "<foreach collection='serverUserIdList' item='serverUserId' open=',' separator=',' close=''>",
        "#{serverUserId}",
        "</foreach>",
        ") and del_flag = 0 and end_time is null",
        "</script>"])
    fun getMultiUserSessionList(@Param("serverUserIdList")serverUserIdList: List<Int>): List<ChatSessionDto>

    @Select("select session_id, open_id, start_time, end_time from t_chat_session " +
            " where server_user_id = #{serverUserId} and open_id = #{openId} and del_flag = 0 order by session_id desc limit 10")
    fun getUserClientSessionList(@Param("serverUserId")serverUserId: Int, @Param("openId")openId: String): List<ChatSessionDto>

    @Select("select session_id, server_user_id from t_chat_session" +
            " where open_id = #{openId} and session_id < #{sessionId} and message_count > 0 and del_flag = 0 " +
            " order by session_id desc limit 1")
    fun getSessionBeforeOne(@Param("openId")openId: String, @Param("sessionId")sessionId: Int): ChatSessionDto?

    @Select("select session_id, server_user_id from t_chat_session where open_id = #{openId}" +
            " and end_time is null and del_flag = 0 order by session_id desc limit 1")
    fun getLatestOpenSession(@Param("openId")openId: String): ChatSessionDto?

    @Update("update t_chat_session set end_time = sysdate(), row_version = row_version + 1" +
            " where session_id = #{sessionId} and del_flag = 0")
    fun closeSession(@Param("sessionId")sessionId: Int): Int

    @Update("update t_chat_session set message_count = message_count + #{messageCount}," +
            " last_access_time = #{lastAccessTime}, row_version = row_version + 1" +
            " where session_id = #{sessionId} and del_flag = 0")
    fun addMsgCount(@Param("sessionId")sessionId: Int, @Param("messageCount")messageCount: Int,
                    @Param("lastAccessTime")lastAccessTime: Date): Int

    @Insert("insert into t_chat_session (server_user_id, open_id, start_time) values (#{serverUserId}, #{openId}, #{startTime})")
    fun addSession(sessionDto: ChatSessionDto): Int

    @Update("update t_chat_session set rank = #{rank} where session_id = #{sessionId} and del_flag = 0")
    fun appraiseSession(@Param("sessionId")sessionId: Int, @Param("rank")rank: Int): Int
}