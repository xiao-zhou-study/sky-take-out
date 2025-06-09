package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username 用户名
     * @return 密码、状态
     */
    @Select(
            """
            
            SELECT
              password,
              status
            FROM
              employee
            WHERE
              username = #{username};
            """
        )
    Employee getByUsername(String username);

}
