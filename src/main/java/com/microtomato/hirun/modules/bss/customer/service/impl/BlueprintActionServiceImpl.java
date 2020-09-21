package com.microtomato.hirun.modules.bss.customer.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.microtomato.hirun.framework.data.TreeNode;
import com.microtomato.hirun.modules.bss.customer.entity.po.BlueprintAction;
import com.microtomato.hirun.modules.bss.customer.mapper.BlueprintActionMapper;
import com.microtomato.hirun.modules.bss.customer.service.IBlueprintActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuhui
 * @since 2020-04-28
 */
@Slf4j
@Service
public class BlueprintActionServiceImpl extends ServiceImpl<BlueprintActionMapper, BlueprintAction> implements IBlueprintActionService {

    @Override
    public List<BlueprintAction> queryBluePrintInfo(String openId, String actionType) {
        List<BlueprintAction> list = this.baseMapper.selectList(new QueryWrapper<BlueprintAction>().lambda()
                .eq(BlueprintAction::getOpenId, openId)
                .in(BlueprintAction::getActionCode, Arrays.asList(actionType.split(",")))
                .orderByDesc(BlueprintAction::getCreateTime));
        return list;
    }

    @Override
    public List<TreeNode> buildFuncTree(String func, String kind) {
        if (StringUtils.isBlank(func) || StringUtils.equals("false", func)) {
            return null;
        }
        JSONArray funcArray = JSONArray.parseArray(func);
        TreeNode rootNode = new TreeNode();
        rootNode.setId("-1");
        rootNode.setTitle("功能蓝图_" + kind);

        List<TreeNode> resultList = new ArrayList<>();
        List<TreeNode> buildTree = this.buildFuncTree1(funcArray);

        if (buildTree != null) {
            rootNode.setChildren(buildTree);
        }
        resultList.add(rootNode);
        return resultList;
    }

    private List<TreeNode> buildFuncTree1(JSONArray funcArray) {
        if(funcArray==null || funcArray.size()<=0){
            return null;
        }
        int size = funcArray.size();
        List<TreeNode> resultList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            JSONObject jsonObject = funcArray.getJSONObject(i);
            TreeNode newTreeNode = new TreeNode();
            newTreeNode.setTitle(jsonObject.getString("name"));
            newTreeNode.setId(jsonObject.getString("caid"));
            String subs = jsonObject.getString("subs");

            if (!StringUtils.equals(subs, "false")) {
                JSONArray subsJsonArray = jsonObject.getJSONArray("subs");
                List<TreeNode>  childList=this.buildFuncTree2(subsJsonArray);
                if(childList!=null){
                    newTreeNode.setChildren(childList);
                }
            }
            resultList.add(newTreeNode);
        }
        return resultList;
    }

    private List<TreeNode> buildFuncTree2(JSONArray funcArray) {
        if(funcArray==null || funcArray.size()<=0){
            return null;
        }
        int size = funcArray.size();
        List<TreeNode> resultList = new ArrayList<>();
        for(int i=0;i<size;i++){
            JSONObject jsonObject = funcArray.getJSONObject(i);
            TreeNode newTreeNode = new TreeNode();
            newTreeNode.setTitle(jsonObject.getString("name"));
            newTreeNode.setId(jsonObject.getString("caid"));
            String subs = jsonObject.getString("subs");

            if (!StringUtils.equals(subs, "false")) {
                JSONArray subsJsonArray = jsonObject.getJSONArray("subs");
                List<TreeNode>  childList=this.buildFuncTree3(subsJsonArray);
                if(childList!=null){
                    newTreeNode.setChildren(childList);
                }
            }

            resultList.add(newTreeNode);
        }
        return resultList;
    }

    private List<TreeNode> buildFuncTree3(JSONArray funcArray) {
        if(funcArray==null || funcArray.size()<=0){
            return null;
        }
        int size = funcArray.size();
        List<TreeNode> resultList = new ArrayList<>();
        for(int i=0;i<size;i++){
            JSONObject jsonObject = funcArray.getJSONObject(i);
            TreeNode newTreeNode = new TreeNode();
            newTreeNode.setTitle(jsonObject.getString("name"));
            newTreeNode.setId(jsonObject.getString("caid"));

            String subs = jsonObject.getString("subs");

            if (!StringUtils.equals(subs, "false")) {
                JSONArray subsJsonArray = jsonObject.getJSONArray("subs");
                List<TreeNode>  childList=this.buildFuncTree4(subsJsonArray);
                if(childList!=null){
                    newTreeNode.setChildren(childList);
                }
            }

            resultList.add(newTreeNode);
        }
        return resultList;
    }

    private List<TreeNode> buildFuncTree4(JSONArray funcArray) {
        if(funcArray==null || funcArray.size()<=0){
            return null;
        }
        int size = funcArray.size();
        List<TreeNode> resultList = new ArrayList<>();
        for(int i=0;i<size;i++){
            JSONObject jsonObject = funcArray.getJSONObject(i);
            TreeNode newTreeNode = new TreeNode();
            newTreeNode.setTitle(jsonObject.getString("name"));
            newTreeNode.setId(jsonObject.getString("caid"));

            String subs = jsonObject.getString("subs");

            if (!StringUtils.equals(subs, "false")) {
                JSONArray subsJsonArray = jsonObject.getJSONArray("subs");
                List<TreeNode>  childList=this.buildFuncTree5(subsJsonArray);
                if(childList!=null){
                    newTreeNode.setChildren(childList);
                }
            }

            resultList.add(newTreeNode);
        }
        return resultList;
    }

    private List<TreeNode> buildFuncTree5(JSONArray funcArray) {
        if(funcArray==null || funcArray.size()<=0){
            return null;
        }
        int size = funcArray.size();
        List<TreeNode> resultList = new ArrayList<>();
        for(int i=0;i<size;i++){
            JSONObject jsonObject = funcArray.getJSONObject(i);
            TreeNode newTreeNode = new TreeNode();
            newTreeNode.setTitle(jsonObject.getString("name"));
            newTreeNode.setId(jsonObject.getString("caid"));

            String subs = jsonObject.getString("subs");

            if (!StringUtils.equals(subs, "false")) {
                JSONArray subsJsonArray = jsonObject.getJSONArray("subs");
                List<TreeNode>  childList=this.buildFuncTree6(subsJsonArray);
                if(childList!=null){
                    newTreeNode.setChildren(childList);
                }
            }

            resultList.add(newTreeNode);
        }
        return resultList;
    }

    private List<TreeNode> buildFuncTree6(JSONArray funcArray) {
        if(funcArray==null || funcArray.size()<=0){
            return null;
        }
        int size = funcArray.size();
        List<TreeNode> resultList = new ArrayList<>();
        for(int i=0;i<size;i++){
            JSONObject jsonObject = funcArray.getJSONObject(i);
            TreeNode newTreeNode = new TreeNode();
            newTreeNode.setTitle(jsonObject.getString("name"));
            newTreeNode.setId(jsonObject.getString("caid"));
            resultList.add(newTreeNode);
        }
        return resultList;
    }

}
