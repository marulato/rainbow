package org.avalon.rainbow.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.avalon.rainbow.admin.entity.Message;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@Mapper
public interface MessageDAO {

    @Select("SELECT * FROM CM_MESSAGE")
    List<Message> selectAll();

    @Select("SELECT * FROM CM_MESSAGE WHERE ID = #{id}")
    Message selectById(Integer id);

    @Select("SELECT * FROM CM_MESSAGE WHERE MSG_KEY = #{key}")
    Message selectByMsgKey(String key);
}
