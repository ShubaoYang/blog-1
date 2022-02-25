package com.minzheng.blog.vo;

import com.minzheng.blog.dto.TagDTO;
import com.minzheng.blog.entity.Question;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

/**
 * @author shubao
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "问答")
public class QuestionVo extends Question {

    private String categoryName;

    private List<TagDTO> tags;

}
