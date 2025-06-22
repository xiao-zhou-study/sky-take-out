package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.vo.EmployeeLoginVO;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO dto
     * @return Employee
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

}
