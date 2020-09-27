package com.microtomato.hirun.modules.college.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeTaskExperienceImgResponseDTO;
import com.microtomato.hirun.modules.college.task.entity.dto.CollegeTaskExperienceRequetDTO;
import com.microtomato.hirun.modules.college.task.entity.po.CollegeTaskExperience;
import com.microtomato.hirun.modules.college.task.mapper.CollegeTaskExperienceMapper;
import com.microtomato.hirun.modules.college.task.service.ICollegeTaskExperienceService;
import com.microtomato.hirun.modules.system.entity.po.UploadFile;
import com.microtomato.hirun.modules.system.service.IUploadFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class CollegeTaskExperienceServiceImpl extends ServiceImpl<CollegeTaskExperienceMapper, CollegeTaskExperience> implements ICollegeTaskExperienceService {

    @Autowired
    private CollegeTaskExperienceMapper collegeTaskExperienceMapper;

    @Autowired
    private IUploadFileService uploadFileServiceImpl;


    @Override
    public CollegeTaskExperienceRequetDTO queryByTaskId(String taskId) {
        CollegeTaskExperienceRequetDTO requetDTO = new CollegeTaskExperienceRequetDTO();
        List<CollegeTaskExperience> list = this.list(Wrappers.<CollegeTaskExperience>lambdaQuery().eq(CollegeTaskExperience::getTaskId, taskId)
                .eq(CollegeTaskExperience::getStatus, "0"));
        if (ArrayUtils.isNotEmpty(list)){
            String writtenExperience = "";
            List<CollegeTaskExperienceImgResponseDTO> imgExperienceList = new ArrayList<>();
            for (CollegeTaskExperience collegeTaskExperience : list){
                if (StringUtils.equals("0", collegeTaskExperience.getExperienceType())){
                    writtenExperience = collegeTaskExperience.getExperienceContent();
                }else if (StringUtils.equals("1", collegeTaskExperience.getExperienceType())){
                    String fileId = collegeTaskExperience.getExperienceContent();
                    UploadFile uploadFile = uploadFileServiceImpl.getByFileId(fileId);
                    if (null != uploadFile){
                        CollegeTaskExperienceImgResponseDTO collegeTaskExperienceImgResponseDTO = new CollegeTaskExperienceImgResponseDTO();
                        collegeTaskExperienceImgResponseDTO.setFileId(fileId);
                        collegeTaskExperienceImgResponseDTO.setFileUrl(uploadFile.getFilePath());
                        imgExperienceList.add(collegeTaskExperienceImgResponseDTO);
                    }
                }
            }
            requetDTO.setImgExperienceList(imgExperienceList);
            requetDTO.setWrittenExperience(writtenExperience);
        }
        return requetDTO;
    }
}