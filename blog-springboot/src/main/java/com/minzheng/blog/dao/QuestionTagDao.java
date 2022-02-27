package com.minzheng.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minzheng.blog.dto.QuestionTagDTO;
import com.minzheng.blog.entity.QuestionTag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionTagDao extends BaseMapper<QuestionTag> {
    int deleteByPrimaryKey(Integer id);

    @Override
    int insert(QuestionTag record);

    int insertSelective(QuestionTag record);

    QuestionTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuestionTag record);

    int updateByPrimaryKey(QuestionTag record);

    List<QuestionTagDTO> selectTagIdsByQuestionId(Integer questionId);
}