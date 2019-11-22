package com.microtomato.hirun.modules.system.service.impl;

import com.microtomato.hirun.modules.system.entity.po.ExcelImportError;
import com.microtomato.hirun.modules.system.mapper.ExcelImportErrorMapper;
import com.microtomato.hirun.modules.system.service.IExcelImportErrorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 导入异常数据 服务实现类
 * </p>
 *
 * @author Steven
 * @since 2019-11-22
 */
@Slf4j
@Service
public class ExcelImportErrorServiceImpl extends ServiceImpl<ExcelImportErrorMapper, ExcelImportError> implements IExcelImportErrorService {

}
