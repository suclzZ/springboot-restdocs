package com.sucl.restdocs.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sucl
 * @since 2019/11/5
 */
@Data(staticConstructor = "of")
@Accessors(chain = true)
public class User implements Serializable {

    private Long userId;

    private String userName;

    private String userCaption;

    private Integer age;

    private String telephone;

    private String email;
}
