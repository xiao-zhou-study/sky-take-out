package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.result.PageResult;
import com.sky.vo.EmployeeLoginVO;
import com.sky.vo.EmployeeUpdateVO;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO dto
     * @return Employee
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     *
     * @param employeeDTO dto
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO dto
     * @return PageResult
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     *
     * @param status 状态
     * @param id     员工id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询员工信息
     *
     * @param id 员工id
     * @return 员工信息
     */
    EmployeeUpdateVO getById(Long id);


    /**
     * 编辑员工信息
     *
     * @param employeeDTO dto
     */
    void update(EmployeeDTO employeeDTO);

    /**
     * 修改密码
     *
     * @param passwordEditDTO dto
     */
    void updatePassword(PasswordEditDTO passwordEditDTO);
}
