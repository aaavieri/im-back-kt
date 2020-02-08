package com.wwx.imback.dao

import com.wwx.imback.dto.table.UserQuickReplyDto
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface UserQuickReplyMapper {

    @Select("select quick_reply_id, server_user_id, type, reply_content from t_user_quick_reply " +
            "where del_flag = 0 and server_user_id = #{serverUserId}")
    fun getUserQuickReply(@Param("serverUserId")serverUserId: Int): List<UserQuickReplyDto>

    @Insert("insert into t_user_quick_reply (server_user_id, type, reply_content) values" +
            " (#{serverUserId}, #{type}, #{replyContent})")
    fun insertQuickReply(quickReplyDto: UserQuickReplyDto): Int

    @Insert("update t_user_quick_reply set reply_content = #{replyContent}, update_time = sysdate(), " +
            "        'row_version = row_version + 1 where quick_reply_id = #{quickReplyId} and del_flag = 0")
    fun updateQuickReply(quickReplyDto: UserQuickReplyDto): Int
}