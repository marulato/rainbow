package org.avalon.rainbow.admin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.avalon.rainbow.admin.entity.MasterCode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MasterCodeDAO {


    @Select("SELECT * FROM CM_MASTER_CODE")
    List<MasterCode> selectAll();

    @Select("SELECT * FROM CM_MASTER_CODE WHERE ID + #{id}")
    MasterCode selectById(Integer id);

    @Select("SELECT * FROM CM_MASTER_CODE WHERE CODE_TYPE = #{param1} AND CODE = #{param2}")
    MasterCode selectByCodeTypeAndCode(String codeType, String code);
}
