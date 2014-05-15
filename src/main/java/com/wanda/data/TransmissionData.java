package com.wanda.data;

public class TransmissionData {

	public TransmissionData(){
		
	}
	
	public TransmissionData(MetaData metaData) {
		super();
		this.metaData = metaData;
	}

	private MetaData metaData;

	public MetaData getMetadata() {
		return metaData;
	}

	public void setMetadata(MetaData metaData) {
		this.metaData = metaData;
	}
}
