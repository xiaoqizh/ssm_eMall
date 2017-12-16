package cn.e3mall.content.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

import java.util.List;

public interface ContentService {

    E3Result addContent(TbContent tbContent);
    List<TbContent> getContentList(long cid);
}
