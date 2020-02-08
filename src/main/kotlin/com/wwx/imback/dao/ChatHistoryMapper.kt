package com.wwx.imback.dao

import com.wwx.imback.dto.table.ChatHistoryDto
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface ChatHistoryMapper {

    @Select(value = ["<script>",
        "select history_id, session_id, message, message_type, type, create_time from t_chat_history where session_id ",
        "in (-1 ",
        "<foreach collection='sessionList' item='sessionId' open=',' separator=',' close=''>",
        "#{sessionId}",
        "</foreach>",
        ") and del_flag = 0 ",
        "</script>"])
    fun getHistoryBySessionList(@Param("sessionList")sessionList: List<Int>): List<ChatHistoryDto>

    @Select(value = ["<script>",
        "select history_id, session_id, message, message_type, type, create_time from t_chat_history where session_id ",
        "in (-1 ",
        "<foreach collection='sessionList' item='sessionId' open=',' separator=',' close=''>",
        "#{sessionId}",
        "</foreach>",
        ") and <foreach collection='keywordList' item='keyword' open='(' separator=' or ' close=')'>",
        "message like #{keyword}",
        "</foreach>",
        "and del_flag = 0 ",
        "</script>"])
    fun getHistoryBySessionAndWord(@Param("sessionList")sessionList: List<Int>,
                                   @Param("keywordList")keywordList: List<String>): List<ChatHistoryDto>

    @Select("select history_id, session_id, message, message_type, type, create_time " +
            "from t_chat_history where session_id = #{sessionId} and del_flag = 0")
    fun getHistoryBySessionId(@Param("sessionId")sessionId: Int): List<ChatHistoryDto>

    @Select(value = ["<script>",
        "insert into t_chat_history (session_id, message, message_type, type, create_time) values ",
        "<foreach collection='messageList' item='oneMessage' open='' separator=',' close=''>",
        "(#{oneMessage.sessionId}, #{oneMessage.message}, #{oneMessage.messageType}, #{oneMessage.type}, #{oneMessage.createTime})",
        "</foreach>",
        "</script>"])
    fun addMessageList(@Param("messageList")messageList: List<ChatHistoryDto>): Int
}