package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl  implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Autowired
	private TbContentMapper contentMapper;
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {	
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> selectByExample = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> nodes =new ArrayList<>();
		for (TbContentCategory tbContentCategory : selectByExample) {
			EasyUITreeNode node =new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			nodes.add(node);
		}	
		return nodes;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		TbContentCategory contentCategory =new TbContentCategory();
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(1);
		contentCategory.setIsParent(false);
		contentCategoryMapper.insert(contentCategory);
       /*根据parentId查询父结点 把父结点变成isParent改了*/
		TbContentCategory pcategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!pcategory.getIsParent())
		{
			pcategory.setIsParent(true);
			//一定不要忘了更新
			contentCategoryMapper.updateByPrimaryKey(pcategory);
		}
		return E3Result.ok(contentCategory);
	}

	/**然后在controller里面调用就行了
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	@Override
	public EasyUIDataGridResult showContents(long categoryId ,int page, int rows) {
		PageHelper pageHelper = new PageHelper();
		pageHelper.startPage(page,rows);
		TbContentExample example =  new TbContentExample();
		TbContentExample.Criteria criteria = example.createCriteria();
		//算了 不用实现了  数据库中根本没有这个方法
		List<TbContent> contents = contentMapper.selectByExample(example);
		PageInfo pageInfo = new PageInfo(contents);
		long total = pageInfo.getTotal();
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		easyUIDataGridResult.setTotal(total);
		easyUIDataGridResult.setRows(contents);
		return easyUIDataGridResult;
	}
}
