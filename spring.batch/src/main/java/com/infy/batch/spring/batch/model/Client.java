package com.infy.batch.spring.batch.model;

import java.io.Serializable;

public class Client implements Serializable {
	private static final long serialVersionUID = -6402068923614583448L;
	private String clientName;

    
    public Client() {
	}

	public Client(String clientName) {
		super();
		this.clientName = clientName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Client [clientName=" + clientName + "]";
	}



}
