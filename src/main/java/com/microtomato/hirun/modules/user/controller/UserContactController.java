package com.microtomato.hirun.modules.user.controller;

import com.microtomato.hirun.modules.user.service.IUserContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jinnian
 * @since 2019-11-02
 */
@RestController
@Slf4j
@RequestMapping("/api/user/user-contact")
public class UserContactController {

    @Autowired
    private IUserContactService userContactServiceImpl;



}
