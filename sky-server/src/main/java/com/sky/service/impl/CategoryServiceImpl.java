package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO 分类分页查询参数
     * @return 分类分页结果
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        try (Page<Category> page = PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize())) {
            List<Category> result = categoryMapper.pageQuery(categoryPageQueryDTO);
            return new PageResult(page.getTotal(), result);
        }
    }

    /**
     * 启用禁用分类
     *
     * @param status 状态
     * @param id     分类id
     */
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.update(category);
    }

    /**
     * 修改分类
     *
     * @param categoryDTO 分类数据
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .id(categoryDTO.getId())
                .type(categoryDTO.getType())
                .name(categoryDTO.getName())
                .sort(categoryDTO.getSort())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 新增分类
     *
     * @param categoryDTO 分类数据
     */
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.insert(category);
    }

    /**
     * 删除分类
     *
     * @param id 分类id
     */
    public void delete(Long id) {
        categoryMapper.deleteById(id);
    }

    /**
     * 查询分类
     * @param type
     * @return
     */
    public List<Category> getByType(String type) {
        return categoryMapper.selectByType(type);
    }
}
