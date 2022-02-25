package com.minzheng.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 文章
 *
 * @author yezhiqiu
 * @date 2021/08/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 作者
     */
    private Integer userId;

    /**
     * 标题
     */
    private String question;

    /**
     * 内容
     */
    private String answer;


    /**
     * 发表时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 文章分类id
     */
    private Integer categoryId;

    private Integer isDelete;

    private String categoryName;

    private List<TagDTO> tags;

}
