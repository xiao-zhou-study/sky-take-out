package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO 分类分页查询参数
     * @return 分类分页结果
     */
    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改分类
     *
     * @param category 分类
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * 新增分类
     *
     * @param category 分类
     */
    @Insert("""
                INSERT INTO category (
                    type,
                    name,
                    sort,
                    status,
                    create_time,
                    update_time,
                    create_user,
                    update_user
                ) VALUES (
                    #{type},
                    #{name},
                    #{sort},
                    #{status},
                    #{createTime},
                    #{updateTime},
                    #{createUser},
                    #{updateUser}
                )
            """)
    @AutoFill(value = OperationType.INSERT)
    void insert(Category category);

    /**
     * 根据id删除分类
     *
     * @param id 分类id
     */
    @Delete("""
                DELETE FROM category WHERE id = #{id}
            """)
    void deleteById(Long id);

    @Select("""
                select * from category where type = #{type}
            """)
    List<Category> selectByType(String type);
}
