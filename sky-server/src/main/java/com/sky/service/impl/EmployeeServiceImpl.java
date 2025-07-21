package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import com.sky.vo.EmployeeUpdateVO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final JwtProperties jwtProperties;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper, JwtProperties jwtProperties) {
        this.employeeMapper = employeeMapper;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 员工登录
     *
     * @param employeeLoginDTO dto 包含用户名、密码
     * @return Employee
     */
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // 1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        if (!BCrypt.checkpw(password, employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 3、登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(), claims);

        // 4、返回VO对象
        return EmployeeLoginVO.builder().id(employee.getId()).userName(employee.getUsername())
                .name(employee.getName()).token(token).build();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 员工登录时传递的数据模型
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setPassword(BCrypt.hashpw(PasswordConstant.DEFAULT_PASSWORD, BCrypt.gensalt()));
        employee.setStatus(StatusConstant.ENABLE);

        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     *
     * @param employeePageQueryDTO 分页查询参数
     * @return 分页结果
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        try (Page<Object> page = PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize())) {
            List<Employee> result = employeeMapper.pageQuery(employeePageQueryDTO);
            return new PageResult(page.getTotal(), result);
        }
    }

    /**
     * 启用禁用员工账号
     *
     * @param status 状态
     * @param id     员工id
     */
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工信息
     *
     * @param id 员工id
     * @return 员工信息
     */
    public EmployeeUpdateVO getById(Long id) {
        EmployeeUpdateVO employeeUpdateVO = new EmployeeUpdateVO();
        BeanUtils.copyProperties(employeeMapper.getById(id), employeeUpdateVO);
        return employeeUpdateVO;
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO 员工信息
     */
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employeeMapper.update(employee);
    }

    /**
     * 修改密码
     *
     * @param passwordEditDTO 修改密码参数
     */
    public void updatePassword(PasswordEditDTO passwordEditDTO) {
        Employee employee = employeeMapper.getByIdPassword(BaseContext.getCurrentId());

        if (!BCrypt.checkpw(passwordEditDTO.getOldPassword(), employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.OLD_PASSWORD_ERROR);
        }

        if (passwordEditDTO.getOldPassword().equals(passwordEditDTO.getNewPassword())) {
            throw new PasswordErrorException(MessageConstant.NEW_PASSWORD_EQUAL_OLD_PASSWORD);
        }

        employee.setPassword(BCrypt.hashpw(passwordEditDTO.getNewPassword(), BCrypt.gensalt()));
        employeeMapper.update(employee);
    }
}
