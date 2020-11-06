package com.microtomato.hirun.modules.college.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microtomato.hirun.framework.util.ArrayUtils;
import com.microtomato.hirun.modules.college.wiki.entity.dto.WikiDetailServiceDTO;
import com.microtomato.hirun.modules.college.wiki.entity.dto.WikiReplyServiceDTO;
import com.microtomato.hirun.modules.college.wiki.entity.dto.WikiServiceDTO;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWikiReply;
import com.microtomato.hirun.modules.college.wiki.mapper.CollegeWikiMapper;
import com.microtomato.hirun.modules.college.wiki.entity.po.CollegeWiki;
import com.microtomato.hirun.modules.college.wiki.service.ICollegeWikiReplyService;
import com.microtomato.hirun.modules.college.wiki.service.ICollegeWikiService;
import com.microtomato.hirun.modules.organization.service.IEmployeeService;
import com.microtomato.hirun.modules.system.service.IStaticDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeWiki)表服务实现类
 *
 * @author huanghua@asiainfo.com
 * @version 1.0.0
 * @date 2020-10-28 02:20:47
 */
@Service("collegeWikiService")
public class CollegeWikiServiceImpl extends ServiceImpl<CollegeWikiMapper, CollegeWiki> implements ICollegeWikiService {

    @Autowired
    private CollegeWikiMapper collegeWikiMapper;

    @Autowired
    private IStaticDataService staticDataServiceImpl;

    @Autowired
    private ICollegeWikiReplyService collegeWikiReplyService;

    @Autowired
    private IEmployeeService employeeService;
    
    @Override
    public List<CollegeWiki> queryByText(String keyStr) {
        return this.list(new QueryWrapper<CollegeWiki>().lambda()
//                .and(q -> q.like(StringUtils.isNotEmpty(keyStr), CollegeWiki::getWikiTitle, keyStr)
//                        .or().like(StringUtils.isNotEmpty(keyStr), CollegeWiki::getWikiContent, keyStr))
                .eq(CollegeWiki::getStatus, "0")
                .orderByDesc(CollegeWiki::getWikiType));
    }

    @Override
    public List<WikiServiceDTO> queryWikiByType(String wikiType) {
        List<WikiServiceDTO> wikiInfos = new ArrayList<>();
        WikiServiceDTO wikiInfo = new WikiServiceDTO();
        List<CollegeWiki> list = this.list(Wrappers.<CollegeWiki>lambdaQuery()
                .eq(CollegeWiki::getWikiType, wikiType)
                .eq(CollegeWiki::getStatus, "0"));
        String wikiTypeName = staticDataServiceImpl.getCodeName("WIKI_TYPE", wikiType);
        wikiInfo.setWikiType(wikiType);
        if (StringUtils.isNotBlank(wikiTypeName)) {
            wikiInfo.setWikiTypeName(wikiTypeName);
        }
        wikiInfo.setWikiList(list);
        wikiInfos.add(wikiInfo);
        return wikiInfos;
    }

    @Override
    public WikiDetailServiceDTO getDetailByWikiId(Long wikiId) {
        WikiDetailServiceDTO wikiDetail = new WikiDetailServiceDTO();
        CollegeWiki wiki = this.getById(wikiId);
        if (null == wiki) {
            return wikiDetail;
        }
        List<WikiReplyServiceDTO> replys = new ArrayList<>();
        List<CollegeWikiReply> replyList = collegeWikiReplyService.queryByWikiId(wikiId);
        if (ArrayUtils.isNotEmpty(replyList)) {
            replyList.stream().forEach(y -> {
                WikiReplyServiceDTO reply = new WikiReplyServiceDTO();
                BeanUtils.copyProperties(y, reply);
                reply.setReplyer(employeeService.getEmployeeNameEmployeeId(reply.getRespondent()));
                replys.add(reply);
            });
        }
        wikiDetail.setReplyInfos(replys);
        wikiDetail.setWiki(wiki);
        wikiDetail.setWikiTypeName(staticDataServiceImpl.getCodeName("WIKI_TYPE", wiki.getWikiType()));
        wikiDetail.setAuthorName(employeeService.getEmployeeNameEmployeeId(wiki.getAuthor()));
        return wikiDetail;
    }
}