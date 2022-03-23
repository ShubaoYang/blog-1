package com.minzheng.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minzheng.blog.dto.*;
import com.minzheng.blog.entity.Article;
import com.minzheng.blog.entity.Question;
import com.minzheng.blog.vo.ConditionVO;
import com.minzheng.blog.vo.QuestionVo;
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
     * 按条件查询问答
     * @param condition 条件
     * @return 总数
     */
    Integer countQuestionBacks(@Param("condition") ConditionVO condition);

    /**
     *  按条件查询问答列表
     * @param current 当前页
     * @param size 分页大小
     * @param condition 条件
     * @return 分页数据
     */
    List<QuestionDTO> listQuestionBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * 查询首页文章
     *
     * @param current 页码
     * @param size    大小
     * @return 文章列表
     */
//    List<QuestionDTO> listQuestions(@Param("current") Long current, @Param("size") Long size);
//
    /**
     * 根据id查询问答
     *
     * @param questionId 文章id
     * @return 问答信息
     */
    QuestionDTO getQuestionById(@Param("questionId") Integer questionId);

    List<QuestionDTO> getBatchQuestions(@Param("size") Integer size);
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
