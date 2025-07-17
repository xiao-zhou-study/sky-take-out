package com.sky.result;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装分页查询结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页结果封装")
public class PageResult implements Serializable {

    @ApiModelProperty("总记录数")
    private long total; // 总记录数

    @ApiModelProperty("当前页数据")
    private List<?> records; // 当前页数据集合

}
