package com.catv.common.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * easyuitree节点类
 */
@Getter
@Setter
public class EasyUITreeNode implements Serializable{

	private Long id;
	private String text;
	private String state;
}
