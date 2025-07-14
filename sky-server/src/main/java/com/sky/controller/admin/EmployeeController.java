package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired(required = false)
    private EmployeeService employeeService;

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
        employeeService.save(employeeDTO);
        return Result.success();
    }

}
