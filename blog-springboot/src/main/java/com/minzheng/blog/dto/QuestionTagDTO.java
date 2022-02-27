package com.minzheng.blog.dto;

import com.minzheng.blog.entity.QuestionTag;
import lombok.*;

/**
 * @author Administrator
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionTagDTO extends QuestionTag {
    private String tagName;
}
