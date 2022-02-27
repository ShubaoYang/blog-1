package com.minzheng.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzheng.blog.dao.QuestionTagDao;
import com.minzheng.blog.dto.QuestionTagDTO;
import com.minzheng.blog.dto.TagDTO;
import com.minzheng.blog.entity.QuestionTag;
import com.minzheng.blog.service.QuestionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shubao
 */
@Service
public class QuestionTagServiceImpl extends ServiceImpl<QuestionTagDao, QuestionTag> implements QuestionTagService {


    @Autowired
    private QuestionTagDao questionTagDao;


    @Override
    public List<TagDTO> getTagsByQuestionId(Integer questionId) {
        List<QuestionTagDTO> questionTags = questionTagDao.selectTagIdsByQuestionId(questionId);
        return questionTags.stream().map(questionTag->TagDTO.builder().id(questionTag.getTagId()).tagName(questionTag.getTagName())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<String> getTagNamesByQuestionId(Integer questionId) {
        List<QuestionTagDTO> questionTags = questionTagDao.selectTagIdsByQuestionId(questionId);
        return questionTags.stream().map(QuestionTagDTO::getTagName).collect(Collectors.toList());
    }
}
