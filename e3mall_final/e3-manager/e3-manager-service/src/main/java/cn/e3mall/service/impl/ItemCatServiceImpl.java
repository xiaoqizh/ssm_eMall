package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;


@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	TbItemCatMapper tbItemCatMapper;
	@Override
	public List<EasyUITreeNode> getCatList(long parentId) {
		//根据parentId来得到下面一层的子节点
		TbItemCatExample example = new TbItemCatExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andParentIdEqualTo(parentId);
		//查询得到所有
		List<TbItemCat> byExample = tbItemCatMapper.selectByExample(example);
		List<EasyUITreeNode> result = new ArrayList<>();
		for (TbItemCat tbItemCat : byExample) {
			EasyUITreeNode easyUITreeNode  = new EasyUITreeNode();
			easyUITreeNode.setId(tbItemCat.getId());
			easyUITreeNode.setText(tbItemCat.getName());
			easyUITreeNode.setState(tbItemCat.getIsParent() ? "closed":"open");
			result.add(easyUITreeNode);
		}
		return result;
	}

}
