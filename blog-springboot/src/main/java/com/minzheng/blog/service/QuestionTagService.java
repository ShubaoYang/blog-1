package com.minzheng.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minzheng.blog.dto.TagDTO;
import com.minzheng.blog.entity.QuestionTag;

import java.util.List;

/**
 * 问答标签Service
 *
 * @author shubao
 */
public interface QuestionTagService extends IService<QuestionTag> {

    List<TagDTO> getTagsByQuestionId(Integer questionId);

    List<String> getTagNamesByQuestionId(Integer questionId);
}
