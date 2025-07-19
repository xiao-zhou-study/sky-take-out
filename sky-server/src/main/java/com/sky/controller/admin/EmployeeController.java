package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import com.sky.vo.EmployeeUpdateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 登录
     *
     * @param employeeLoginDTO 登录信息
     * @return Result<EmployeeLoginVO>
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        return Result.success(employeeService.login(employeeLoginDTO));
    }

    /**
     * 退出
     *
     * @return Result<String>
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<Void> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 员工信息
     * @return Result<Void>
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result<Void> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO 分页查询参数
     * @return Result<PageResult>
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工账号
     *
     * @param status 状态
     * @param id     员工id
     * @return Result<Void>
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result<Void> startOrStop(@PathVariable("status") Integer status, Long id) {
        log.info("员工状态：{}，员工id：{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据id查询员工信息
     *
     * @param id 员工id
     * @return Result<EmployeeUpdateVO>
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public Result<EmployeeUpdateVO> getById(@PathVariable("id") Long id) {
        return Result.success(employeeService.getById(id));
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO 员工信息
     * @return Result<Void>
     */
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result<Void> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("要修改的员工信息：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }


}
