package com.microtomato.hirun.func;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.microtomato.hirun.HirunHelperApplication;
import com.microtomato.hirun.modules.user.entity.po.UserFunc;
import com.microtomato.hirun.modules.user.service.IUserFuncService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 扫描 ins_user_func 表，将权限数据相同的进行归并。
 * 目前发现可以归并到 56 组。
 *
 * 最终目标：
 * 将 ins_user_func 表的数据收敛到 ins_role 和 ins_user_role, ins_func_role, ins_menu_role 四张表里。
 *
 * @author Steven
 * @date 2019-11-17
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HirunHelperApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FuncMerge {

    @Autowired
    public IUserFuncService userFuncServiceImpl;

    // user_id 为 key，权限 Set 为 value
    private static final Map<Long, UserData> USER_FUNC_MAP = new HashMap<>();

    // 用户数据
    private static class UserData {

        /**
         * 用户 Id
         */
        private Long userId;

        /**
         * 用户的权限集合
         */
        private TreeSet<Long> funcSet;

        /**
         * 是否处理完
         */
        private boolean completed = false;
    }

    private static final List<FuncGroup> FUNC_GROUP_LIST = new ArrayList<>();

    /**
     * 权限组
     */
    private static class FuncGroup {

        /**
         * 权限组下有哪些用户
         */
        private Set<Long> userIdList = new TreeSet<>();

        /**
         * 权限组下有哪些权限
         */
        private Set<Long> funcIdList;

    }

    @Test
    public void analyse() {

        List<UserFunc> userFuncList = userFuncServiceImpl.list(
            Wrappers.<UserFunc>lambdaQuery()
                .select(UserFunc::getUserId, UserFunc::getFuncId)
                .lt(UserFunc::getStartDate, LocalDateTime.now())
                .gt(UserFunc::getEndDate, LocalDateTime.now()).orderByAsc(UserFunc::getUserId)
        );

        // 遍历 ins_user_func 表，收集每个用户的权限
        for (UserFunc userFunc : userFuncList) {
            Long userId = userFunc.getUserId();
            Long funcId = userFunc.getFuncId();
            UserData userData = USER_FUNC_MAP.get(userId);
            if (null == userData) {
                // 不存在对应的用户数据，构造一个用户数据。
                userData = new UserData();
                userData.userId = userId;
                userData.completed = false;
                userData.funcSet = new TreeSet<>();
            }
            userData.funcSet.add(funcId);
            USER_FUNC_MAP.put(userFunc.getUserId(), userData);
        }

        merge();

    }

    private void merge() {
        for (UserData outer : USER_FUNC_MAP.values()) {
            if (outer.completed) {
                continue;
            }

            FuncGroup funcGroup = new FuncGroup();
            funcGroup.userIdList.add(outer.userId);
            funcGroup.funcIdList = outer.funcSet;
            FUNC_GROUP_LIST.add(funcGroup);

            for (UserData inner : USER_FUNC_MAP.values()) {
                if (inner.completed) {
                    continue;
                }

                if (isEquals(inner.funcSet, outer.funcSet)) {
                    // 权限集合相同
                    inner.completed = true;
                    funcGroup.userIdList.add(inner.userId);
                }
            }
            outer.completed = true;
        }

        System.out.println("============ 汇总数据 ============");
        for (FuncGroup funcGroup : FUNC_GROUP_LIST) {
            System.out.println("userId: " + funcGroup.userIdList);
            System.out.println("    funcId: " + funcGroup.funcIdList);
        }
    }

    private boolean isEquals(Set<Long> s1, Set<Long> s2) {
        if (s1.size() != s2.size()) {
            return false;
        }

        Iterator<Long> iter1 = s1.iterator();
        Iterator<Long> iter2 = s2.iterator();

        while (iter1.hasNext() && iter2.hasNext()) {
            Long next1 = iter1.next();
            Long next2 = iter2.next();
            if (!next1.equals(next2)) {
                return false;
            }
        }

        return true;
    }

}
