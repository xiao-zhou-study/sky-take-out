package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired(required = false))
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;
    private final DishFlavorsMapper dishFlavorsMapper;
    private final SetmealDishMapper setmealDishMapper;

    /**
     * 保存菜品
     * @param dishDTO 菜品DTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        // 向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);

        // 向口味表插入n条数据
        Long id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();

        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(df -> {
                df.setDishId(id);
            });
            dishFlavorsMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        try(Page<DishVO> page = PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize())){
            List<DishVO> result = dishMapper.page(dishPageQueryDTO);
            long total = page.getTotal();
            return new PageResult(total,result);
        }
    }

    /**
     * 菜品批量删除
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断要删除的菜品中有没有起售状态的
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断当前菜品有没有关联套餐
        List<Long> setmealDishIds = setmealDishMapper.getSetmealDishIdsByDishId(ids);
        if (setmealDishIds != null && !setmealDishIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 根据id删除对应菜品
        dishMapper.deleteByIds(ids);

        // 根据id删除对应口味
        dishFlavorsMapper.deleteByDishIds(ids);

    }

    /**
     * 根据id查询菜品和对应的口味
     *
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavors(Long id) {
        // 根据id查询菜品数据
        Dish dish = dishMapper.getById(id);
        // 根据id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorsMapper.getByDishId(id);
        // 创建DishVO对象，用于封装返回数据
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        // 删除当前菜品对应的口味数据
        dishFlavorsMapper.deleteByDishId(dishDTO.getId());
        // 重新插入当前菜品对应的口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(df -> {
                df.setDishId(dishDTO.getId());
            });
            dishFlavorsMapper.insertBatch(flavors);
        }
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    public List<Dish> getByCategoryId(String categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

    /**
     * 启售停售菜品
     *
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }
}
