package com.test.blogsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * 博客基本信息
 * </p>
 *
 * @author 11903990213李列伟
 * @since 2021-11-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("blog_personal_inform")
@ApiModel(value="BlogPersonalInform对象", description="博客基本信息")
public class BlogPersonalInform implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "博客id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "博客昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty(value = "博客账号密码")
    @TableField("password")
    private String password;

//    @ApiModelProperty(value = "博客手机号")
//    @TableField("phone_number")
//    private String phoneNumber;

    @ApiModelProperty(value = "邮箱")
    @TableField("mailbox")
    private String mailbox;

    @ApiModelProperty(value = "角色")
    @TableField("role")
    private String role = "user";

    @ApiModelProperty(value = "头像路径")
    @TableField("head_portrait")
    private String headPortrait = "http://r3khhc8h0.hn-bkt.clouddn.com/defaulthead.jpg";

    @ApiModelProperty(value = "账号状态")
    @TableField("state")
    private Integer state = 1;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String username;

}
