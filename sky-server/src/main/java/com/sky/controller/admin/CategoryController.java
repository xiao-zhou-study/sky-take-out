package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO 分类分页查询参数
     * @return 分类分页查询结果
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分页查询：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用分类
     *
     * @param status 状态
     * @param id     分类id
     * @return 启用禁用结果
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result<Void> startOrStop(@PathVariable("status") Integer status, Long id) {
        log.info("启用禁用分类：{}", id);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @param categoryDTO 修改的分类数据
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result<Void> update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }


    /**
     * 新增分类
     *
     * @param categoryDTO 新增的分类数据
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result<Void> save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     *
     * @param id 分类id
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result<Void> delete(Long id) {
        log.info("删除分类：{}", id);
        categoryService.delete(id);
        return Result.success();
    }


}
