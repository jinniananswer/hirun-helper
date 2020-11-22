package com.microtomato.hirun.modules.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.bss.order.entity.dto.CascadeDTO;
import com.microtomato.hirun.modules.finance.entity.po.FinanceItem;
import com.microtomato.hirun.modules.finance.mapper.FinanceItemMapper;
import com.microtomato.hirun.modules.finance.service.IFinanceItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 财务科目表(FinanceItem)表服务实现类
 *
 * @author Jinnian
 * @version 1.0.0
 * @date 2020-07-19 22:26:54
 */
@Service
@Slf4j
public class FinanceItemServiceImpl extends ServiceImpl<FinanceItemMapper, FinanceItem> implements IFinanceItemService {

    @Autowired
    private FinanceItemMapper financeItemMapper;

    /**
     * 初始化会计科目
     *
     * @return
     */
    @Override
    public List<CascadeDTO<FinanceItem>> loadFinanceItems() {
        List<FinanceItem> financeItemCfgs = this.queryAll();
        List<CascadeDTO<FinanceItem>> financeItems = this.buildFinanceItemCascade(financeItemCfgs);
        return financeItems;
    }

    /**
     * 查询所有财务科目
     * @return
     */

    @Cacheable(value = "financeItem-all")
    public List<FinanceItem> queryAll() {
        List<FinanceItem> financeItem = this.list(new QueryWrapper<FinanceItem>().lambda().eq(FinanceItem::getStatus, "U"));
        return financeItem;
    }

    @Cacheable(value = "finance_item")
    @Override
    public FinanceItem getByFinanceItemId(String financeItemId) {
        return this.getOne(new QueryWrapper<FinanceItem>().lambda().eq(FinanceItem::getFinanceItemId, financeItemId).eq(FinanceItem::getStatus, "U"));
    }

    /**
     * 构建支付类型选项树
     *
     * @return
     */
    private List<CascadeDTO<FinanceItem>> buildFinanceItemCascade(List<FinanceItem> financeItemCfgs) {
        if (ArrayUtils.isEmpty(financeItemCfgs)) {
            return null;
        }
        List<CascadeDTO<FinanceItem>> roots = new ArrayList<>();
        for (FinanceItem financeItemCfg : financeItemCfgs) {
            if (StringUtils.isBlank(financeItemCfg.getParentFinanceItemId())) {
                CascadeDTO<FinanceItem> root = new CascadeDTO<>();
                root.setLabel(financeItemCfg.getName());
                root.setValue(financeItemCfg.getFinanceItemId());
                roots.add(root);
            }
        }
        if (ArrayUtils.isNotEmpty(roots)) {
            for (CascadeDTO<FinanceItem> root : roots) {
                this.buildFinanceItemChildren(root, financeItemCfgs);
            }
        }
        return roots;
    }

    /**
     * 构建子孙节点
     *
     * @param root
     * @param financeItemCfgs
     * @return
     */
    private void buildFinanceItemChildren(CascadeDTO root, List<FinanceItem> financeItemCfgs) {
        List<CascadeDTO<FinanceItem>> children = new ArrayList<>();
        for (FinanceItem financeItemCfg : financeItemCfgs) {
            if (StringUtils.equals(financeItemCfg.getParentFinanceItemId(), root.getValue())) {
                CascadeDTO<FinanceItem> child = new CascadeDTO<>();
                child.setLabel(financeItemCfg.getName());
                child.setValue(financeItemCfg.getFinanceItemId());
                children.add(child);

                buildFinanceItemChildren(child, financeItemCfgs);
            }
        }
        if (ArrayUtils.isNotEmpty(children)) {
            root.setChildren(children);
        }
    }
}