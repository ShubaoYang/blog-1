package com.minzheng.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minzheng.blog.dto.*;
import com.minzheng.blog.entity.Article;
import com.minzheng.blog.entity.Question;
import com.minzheng.blog.vo.*;

import java.util.List;

/**
 * 文章服务
 *
 * @author yezhiqiu
 * @date 2021/07/29
 */
public interface QuestionService extends IService<Question> {


    /**
     * 创建或更新问答
     * @param questionVo 问答
     */
    void saveOrUpdateQuestion(QuestionVo questionVo);

    /**
     * 后台查询问答列表
     * @param condition
     * @return
     */
    PageResult<QuestionVo> listQuestionBacks(ConditionVO condition);

}
