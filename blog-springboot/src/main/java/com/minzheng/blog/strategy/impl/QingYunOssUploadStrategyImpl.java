package com.minzheng.blog.strategy.impl;

import com.minzheng.blog.config.QingYunOssProperties;
import com.qingstor.sdk.config.EnvContext;
import com.qingstor.sdk.exception.QSException;
import com.qingstor.sdk.service.Bucket;
import com.qingstor.sdk.service.QingStor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * oss上传策略
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Service("qingYunOssUploadStrategyImpl")
public class QingYunOssUploadStrategyImpl extends AbstractUploadStrategyImpl {
    @Autowired
    private QingYunOssProperties qingYunOssProperties;

    @Override
    public Boolean exists(String filePath) throws QSException {
        Bucket.GetObjectOutput file = bucket().getObject(filePath, null);
        return null == file;
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        Bucket.PutObjectInput input = new Bucket.PutObjectInput();
        input.setBodyInputStream(inputStream);
        try {
            Bucket.PutObjectOutput output = bucket().putObject(path + fileName, input);
            if (output.getStatueCode() == 201) {
                System.out.println("PUT Object OK.");
                System.out.println("key = " + path);
            } else {
                // Failed
                System.out.println("Failed to PUT object.");
                System.out.println("key = " + path);
                System.out.println("StatueCode = " + output.getStatueCode());
                System.out.println("Message = " + output.getMessage());
                System.out.println("RequestId = " + output.getRequestId());
                System.out.println("Code = " + output.getCode());
                System.out.println("Url = " + output.getUrl());
            }
        } catch (QSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return qingYunOssProperties.getHost() + filePath;
    }


    private QingStor qingStor() {
        return new QingStor(getEnvContext());
    }

    private Bucket bucket() {
        return new Bucket(getEnvContext(), qingYunOssProperties.getZoneKey(), qingYunOssProperties.getBucketName());
    }

    private EnvContext getEnvContext() {
        return new EnvContext(qingYunOssProperties.getAccess_key_id(), qingYunOssProperties.getSecret_access_key());
    }

}
