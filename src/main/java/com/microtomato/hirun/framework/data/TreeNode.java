package com.microtomato.hirun.framework.data;

import lombok.Data;

import java.util.List;

/**
 * @program: hirun-helper
 * @description: 通用树节点对象
 * @author: jinnian
 * @create: 2019-09-08 15:32
 **/
@Data
public class TreeNode<T> {

    private String id;

    private String parentId;

    T node;

    List<TreeNode> children;
}
