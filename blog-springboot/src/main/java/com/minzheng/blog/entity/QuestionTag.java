package com.minzheng.blog.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tb_question_tag
 * @author shubao
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_question_tag")
public class QuestionTag implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章id
     */
    private Integer questionId;

    /**
     * 标签id
     */
    private Integer tagId;

    private static final long serialVersionUID = 1L;
}