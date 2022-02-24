package com.minzheng.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minzheng.blog.dto.*;
import com.minzheng.blog.entity.Article;
import com.minzheng.blog.entity.Question;
import com.minzheng.blog.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 问题
 *
 * @author shubao
 * @date 2022/02/21
 */
@Repository
public interface QuestionDao extends BaseMapper<Question> {

    /**
     * 查询首页文章
     *
     * @param current 页码
     * @param size    大小
     * @return 文章列表
     */
//    List<QuestionDTO> listQuestions(@Param("current") Long current, @Param("size") Long size);
//
//    /**
//     * 根据id查询文章
//     *
//     * @param articleId 文章id
//     * @return 文章信息
//     */
//    QuestionDTO getQuestionById(@Param("questionId") Integer questionId);
//
//    /**
//     * 根据条件查询文章
//     *
//     * @param current   页码
//     * @param size      大小
//     * @param condition 条件
//     * @return 文章列表
//     */
//    List<QuestionDTO> listQuestionByCondition(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);



}