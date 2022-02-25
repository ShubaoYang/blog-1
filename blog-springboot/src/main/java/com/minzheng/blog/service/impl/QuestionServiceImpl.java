package com.minzheng.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzheng.blog.dao.*;
import com.minzheng.blog.dto.*;
import com.minzheng.blog.entity.*;
import com.minzheng.blog.service.*;
import com.minzheng.blog.util.BeanCopyUtils;
import com.minzheng.blog.util.PageUtils;
import com.minzheng.blog.util.UserUtils;
import com.minzheng.blog.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.minzheng.blog.constant.RedisPrefixConst.*;


/**
 * 文章服务
 *
 * @author yezhiqiu
 * @date 2021/08/10
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionDao, Question> implements QuestionService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private QuestionDao questionDao;


    @Override
    public void saveOrUpdateQuestion(QuestionVo questionVo) {
        // 保存文章分类
        Category category = categoryService.saveOrUpdateCategory(questionVo.getCategoryName());
        // 保存或修改文章
        Question question = BeanCopyUtils.copyObject(questionVo, Question.class);
        if (Objects.nonNull(category)) {
            question.setCategoryId(category.getId());
        }
        question.setUserId(UserUtils.getLoginUser().getUserInfoId());
        this.saveOrUpdate(question);
        // 保存文章标签
//        saveQuestionTag(questionVo, question.getId());
    }

    @Override
    public PageResult<QuestionVo> listQuestionBacks(ConditionVO condition) {
        // 查询文章总量
        Integer count = questionDao.countQuestionBacks(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台文章
        List<QuestionDTO> questions = questionDao.listQuestionBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);

        List<QuestionVo> questionVo = BeanCopyUtils.copyList(questions, QuestionVo.class);
        return new PageResult<QuestionVo>(questionVo, count);
    }

    //    private void saveQuestionTag(QuestionVo questionVo, Integer id) {
//        // 编辑文章则删除文章所有标签
//        if (Objects.nonNull(questionVo.getId())) {
//            articleTagDao.delete(new LambdaQueryWrapper<ArticleTag>()
//                    .eq(ArticleTag::getArticleId, articleVO.getId()));
//        }
//        // 添加文章标签
//        List<String> tagNameList = articleVO.getTagNameList();
//        if (CollectionUtils.isNotEmpty(tagNameList)) {
//            // 查询已存在的标签
//            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>()
//                    .in(Tag::getTagName, tagNameList));
//            List<String> existTagNameList = existTagList.stream()
//                    .map(Tag::getTagName)
//                    .collect(Collectors.toList());
//            List<Integer> existTagIdList = existTagList.stream()
//                    .map(Tag::getId)
//                    .collect(Collectors.toList());
//            // 对比新增不存在的标签
//            tagNameList.removeAll(existTagNameList);
//            if (CollectionUtils.isNotEmpty(tagNameList)) {
//                List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder()
//                        .tagName(item)
//                        .build())
//                        .collect(Collectors.toList());
//                tagService.saveBatch(tagList);
//                List<Integer> tagIdList = tagList.stream()
//                        .map(Tag::getId)
//                        .collect(Collectors.toList());
//                existTagIdList.addAll(tagIdList);
//            }
//            // 提取标签id绑定文章
//            List<ArticleTag> articleTagList = existTagIdList.stream().map(item -> ArticleTag.builder()
//                    .articleId(articleId)
//                    .tagId(item)
//                    .build())
//                    .collect(Collectors.toList());
//            articleTagService.saveBatch(articleTagList);
//        }
//    }
}
