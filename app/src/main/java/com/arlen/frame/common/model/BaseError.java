package com.arlen.frame.common.model;

import java.io.Serializable;

public class BaseError implements Serializable {

	public int status;
	public String data;

	public boolean isSuccess() {
		return status == 0;
	}
}
