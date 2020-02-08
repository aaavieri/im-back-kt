package com.wwx.imback.dao

import com.wwx.imback.dto.table.ChannelQuickReplyDto
import com.wwx.imback.dto.table.UserQuickReplyDto
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface ChannelQuickReplyMapper {

    @Select("select quick_reply_id, channel_id, type, reply_content from t_channel_quick_reply where channel_id = #{channelId}" +
            "      ' and type = #{type} and del_flag = 0 order by quick_reply_id limit 1")
    fun getUserQuickReplyByType(@Param("channelId")channelId: Int, @Param("type")type: Int): ChannelQuickReplyDto?
}