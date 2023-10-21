package com.yupi.jinapi.common;

import lombok.Data;

import java.io.Serializable;

/**
 * ID请求
 *
 * @author snajin
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}