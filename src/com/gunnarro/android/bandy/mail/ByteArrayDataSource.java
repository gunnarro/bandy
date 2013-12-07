package com.gunnarro.android.bandy.mail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class ByteArrayDataSource implements DataSource {

	private byte[] data;
	private String type;

	public ByteArrayDataSource(byte[] data, String type) {
		super();
		this.data = data;
		this.type = type;
	}

	public ByteArrayDataSource(byte[] data) {
		super();
		this.data = data;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getContentType() {
		if (type == null) {
			return "application/octet-stream";
		} else {
			return type;
		}
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(data);
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new IOException("Not Supported");
	}

}
