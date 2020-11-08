package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.mybatis.DataSourceKey;
import com.microtomato.hirun.framework.mybatis.annotation.DataSource;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.framework.util.TimeUtils;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeTaskExperienceImgResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeTaskExperienceScoreResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskExperience;
import com.microtomato.hirun.modules.college.task.mapper.CollegeTaskExperienceMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeTaskExperienceService;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeTaskExperience)表服务实现类
 *
 * @author makejava
 * @version 1.0.0
 * @date 2020-09-27 03:36:43
 */
@Service("collegeTaskExperienceService")
@DataSource(DataSourceKey.INS)
public class CollegeTaskExperienceServiceImpl extends ServiceImpl<CollegeTaskExperienceMapper, CollegeTaskExperience> implements ICollegeTaskExperienceService {

    @Autowired
    private CollegeTaskExperienceMapper collegeTaskExperienceMapper;

    @Autowired
    private IUploadFileService uploadFileServiceImpl;


    @Override
    public CollegeTaskExperienceScoreResponseDTO queryByTaskId(String taskId) {
        CollegeTaskExperienceScoreResponseDTO requetDTO = new CollegeTaskExperienceScoreResponseDTO();
        List<CollegeTaskExperience> list = this.list(Wrappers.<CollegeTaskExperience>lambdaQuery().eq(CollegeTaskExperience::getTaskId, taskId)
                .eq(CollegeTaskExperience::getStatus, "0"));
        if (ArrayUtils.isNotEmpty(list)){
            String writtenExperience = "";
            List<String> imgExperienceList = new ArrayList<>();
            List<String> experienceDescImgList = new ArrayList<>();
            for (CollegeTaskExperience collegeTaskExperience : list){
                if (StringUtils.equals("0", collegeTaskExperience.getExperienceType())){
                    if (StringUtils.equals("0", collegeTaskExperience.getDescType())){
                        writtenExperience = collegeTaskExperience.getExperienceContent();
                    }else if (StringUtils.equals("1", collegeTaskExperience.getDescType())){
                        String fileId = collegeTaskExperience.getExperienceContent();
                        UploadFile uploadFile = uploadFileServiceImpl.getByFileId(fileId);
                        if (null != uploadFile){
                            String fileUrl = uploadFileServiceImpl.getDisplayPath(fileId);
                            experienceDescImgList.add(fileUrl);
                        }
                    }
                }else if (StringUtils.equals("1", collegeTaskExperience.getExperienceType())){
                    String fileId = collegeTaskExperience.getExperienceContent();
                    UploadFile uploadFile = uploadFileServiceImpl.getByFileId(fileId);
                    if (null != uploadFile){
                        String fileUrl = uploadFileServiceImpl.getDisplayPath(fileId);
                        imgExperienceList.add(fileUrl);
                    }
                }
            }
            requetDTO.setImgExperienceList(imgExperienceList);
            requetDTO.setWrittenExperience(writtenExperience);
            requetDTO.setExperienceDescImgList(experienceDescImgList);
        }
        return requetDTO;
    }

    @Override
    public void addExperience(Long taskId, String experience, List<String> fileIdList, List<String> experienceImgList, LocalDateTime now) {
        List<CollegeTaskExperience> entityList = new ArrayList<>();
        if (StringUtils.isNotEmpty(experience)){
            CollegeTaskExperience experienceEntity = new CollegeTaskExperience();
            experienceEntity.setTaskId(String.valueOf(taskId));
            experienceEntity.setExperienceType("0");
            experienceEntity.setExperienceContent(experience);
            experienceEntity.setStatus("0");
            experienceEntity.setDescType("0");
            experienceEntity.setCreateTime(now);
            experienceEntity.setUpdateTime(now);
            entityList.add(experienceEntity);
        }

        if (ArrayUtils.isNotEmpty(experienceImgList)){
            for (String fileId : experienceImgList) {
                CollegeTaskExperience fileEntity = new CollegeTaskExperience();
                fileEntity.setTaskId(String.valueOf(taskId));
                fileEntity.setExperienceType("0");
                fileEntity.setExperienceContent(fileId);
                fileEntity.setStatus("0");
                fileEntity.setDescType("1");
                fileEntity.setCreateTime(now);
                fileEntity.setUpdateTime(now);
                entityList.add(fileEntity);
            }
        }

        for (String fileId : fileIdList) {
            CollegeTaskExperience fileEntity = new CollegeTaskExperience();
            fileEntity.setTaskId(String.valueOf(taskId));
            fileEntity.setExperienceType("1");
            fileEntity.setExperienceContent(fileId);
            fileEntity.setStatus("0");
            fileEntity.setCreateTime(now);
            fileEntity.setUpdateTime(now);
            entityList.add(fileEntity);
        }
        this.saveBatch(entityList);
    }
}