package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.sky.entity.Employee;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工信息
     *
     * @param username 用户名
     * @return 包含员工完整信息的实体对象
     */
    @Select("""
                SELECT
                    id,
                    username,
                    name,
                    password,
                    status
                FROM employee
                WHERE username = #{username}
            """)
    Employee getByUsername(String username);

    /**
     * 插入新员工记录
     *
     * @param employee 员工实体对象
     */
    @Insert("""
                INSERT INTO employee (
                    username,
                    name,
                    password,
                    phone,
                    sex,
                    id_number,
                    status,
                    create_time,
                    update_time,
                    create_user,
                    update_user
                ) VALUES (
                    #{username},
                    #{name},
                    #{password},
                    #{phone},
                    #{sex},
                    #{idNumber},
                    #{status},
                    #{createTime},
                    #{updateTime},
                    #{createUser},
                    #{updateUser}
                )
            """)
    void insert(Employee employee);

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void update(Employee employee);
}
