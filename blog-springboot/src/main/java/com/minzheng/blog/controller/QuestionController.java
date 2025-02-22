package com.minzheng.blog.controller;


import com.minzheng.blog.annotation.OptLog;
import com.minzheng.blog.dto.*;
import com.minzheng.blog.entity.Question;
import com.minzheng.blog.enums.FilePathEnum;
import com.minzheng.blog.service.ArticleService;
import com.minzheng.blog.service.QuestionService;
import com.minzheng.blog.strategy.context.UploadStrategyContext;
import com.minzheng.blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static com.minzheng.blog.constant.OptTypeConst.*;

/**
 * 问题控制器
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Api(tags = "问题模块")
@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;


    /**
     * 添加或修改文章
     *
     * @param articleVO 文章信息
     * @return {@link Result<>}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改问答")
    @PostMapping("/admin/questions")
    public Result<?> saveOrUpdateQuestion(@Valid @RequestBody QuestionVo questionVo) {
        questionService.saveOrUpdateQuestion(questionVo);
        return Result.ok();
    }

    /**
     * 查看后台文章
     *
     * @param conditionVO 条件
     * @return {@link Result<ArticleBackDTO>} 后台文章列表
     */
    @ApiOperation(value = "后台查看问答")
    @GetMapping("/admin/questions")
    public Result<PageResult<QuestionVo>> listQuestionBacks(ConditionVO conditionVO) {
        return Result.ok(questionService.listQuestionBacks(conditionVO));
    }

    /**
     * 根据id查看后台文章
     *
     * @param questionId 文章id
     * @return {@link Result<ArticleVO>} 后台文章
     */
    @ApiOperation(value = "根据id查看后台问答")
    @ApiImplicitParam(name = "questionId", value = "问答id", required = true, dataType = "Integer")
    @GetMapping("/admin/questions/{questionId}")
    public Result<QuestionVo> getQuestionBackById(@PathVariable("questionId") Integer questionId) {
        return Result.ok(questionService.getQuestionBackById(questionId));
    }

    /**
     * 根据id查看后台文章
     *
     * @param questionId 文章id
     * @return {@link Result<ArticleVO>} 后台文章
     */
    @ApiOperation(value = "根据id查看后台问答")
    @ApiImplicitParam(name = "questionId", value = "问答id", required = true, dataType = "Integer")
    @GetMapping("/question/{questionId}")
    public Result<QuestionVo> getQuestionById(@PathVariable("questionId") Integer questionId) {
        return Result.ok(questionService.getQuestionBackById(questionId));
    }

    /**
     * 恢复或删除问答
     *
     * @param deleteVO 逻辑删除信息
     * @return {@link Result<>}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "恢复或删除问答")
    @PutMapping("/admin/questions")
    public Result<?> updateQuestionDelete(@Valid @RequestBody DeleteVO deleteVO) {
        questionService.updateQuestionDelete(deleteVO);
        return Result.ok();
    }

    /**
     * 根据id查看后台文章
     *
     * @return {@link Result<List<QuestionVo>>} 随机返回10道题
     */
    @ApiOperation(value = "批次获取问题")
    @GetMapping("/questions/batch")
    public Result<List<QuestionVo>> getQuestionsBatch() {
        return Result.ok(questionService.getQuestionsBatch());
    }



//
//    /**
//     * 查看文章归档
//     *
//     * @return {@link Result<ArchiveDTO>} 文章归档列表
//     */
//    @ApiOperation(value = "查看文章归档")
//    @GetMapping("/articles/archives")
//    public Result<PageResult<ArchiveDTO>> listArchives() {
//        return Result.ok(articleService.listArchives());
//    }
//
//    /**
//     * 查看首页文章
//     *
//     * @return {@link Result<ArticleHomeDTO>} 首页文章列表
//     */
//    @ApiOperation(value = "查看首页文章")
//    @GetMapping("/articles")
//    public Result<List<ArticleHomeDTO>> listArticles() {
//        return Result.ok(articleService.listArticles());
//    }
//

//
//    /**
//     * 修改文章置顶状态
//     *
//     * @param articleTopVO 文章置顶信息
//     * @return {@link Result<>}
//     */
//    @OptLog(optType = UPDATE)
//    @ApiOperation(value = "修改文章置顶")
//    @PutMapping("/admin/articles/top")
//    public Result<?> updateArticleTop(@Valid @RequestBody ArticleTopVO articleTopVO) {
//        articleService.updateArticleTop(articleTopVO);
//        return Result.ok();
//    }
//
//    /**
//     * 恢复或删除文章
//     *
//     * @param deleteVO 逻辑删除信息
//     * @return {@link Result<>}
//     */
//    @OptLog(optType = UPDATE)
//    @ApiOperation(value = "恢复或删除文章")
//    @PutMapping("/admin/articles")
//    public Result<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO) {
//        articleService.updateArticleDelete(deleteVO);
//        return Result.ok();
//    }
//
//    /**
//     * 上传文章图片
//     *
//     * @param file 文件
//     * @return {@link Result<String>} 文章图片地址
//     */
//    @ApiOperation(value = "上传文章图片")
//    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
//    @PostMapping("/admin/articles/images")
//    public Result<String> saveArticleImages(MultipartFile file) {
//        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.ARTICLE.getPath()));
//    }
//

//

//
//    /**
//     * 根据id查看文章
//     *
//     * @param articleId 文章id
//     * @return {@link Result<ArticleDTO>} 文章信息
//     */
//    @ApiOperation(value = "根据id查看文章")
//    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
//    @GetMapping("/articles/{articleId}")
//    public Result<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
//        return Result.ok(articleService.getArticleById(articleId));
//    }
//
//    /**
//     * 根据条件查询文章
//     *
//     * @param condition 条件
//     * @return {@link Result<ArticlePreviewListDTO>} 文章列表
//     */
//    @ApiOperation(value = "根据条件查询文章")
//    @GetMapping("/articles/condition")
//    public Result<ArticlePreviewListDTO> listArticlesByCondition(ConditionVO condition) {
//        return Result.ok(articleService.listArticlesByCondition(condition));
//    }
//
//    /**
//     * 搜索文章
//     *
//     * @param condition 条件
//     * @return {@link Result<ArticleSearchDTO>} 文章列表
//     */
//    @ApiOperation(value = "搜索文章")
//    @GetMapping("/articles/search")
//    public Result<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
//        return Result.ok(articleService.listArticlesBySearch(condition));
//    }
//
//    /**
//     * 点赞文章
//     *
//     * @param articleId 文章id
//     * @return {@link Result<>}
//     */
//    @ApiOperation(value = "点赞文章")
//    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
//    @PostMapping("/articles/{articleId}/like")
//    public Result<?> saveArticleLike(@PathVariable("articleId") Integer articleId) {
//        articleService.saveArticleLike(articleId);
//        return Result.ok();
//    }

}

