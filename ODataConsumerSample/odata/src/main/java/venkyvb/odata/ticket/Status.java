package main.java.venkyvb.odata.ticket;

import java.util.Optional;

public enum Status {

	NEW("1"), IN_PROCESS("2"), COMPLETED("3"), CLOSED("4");

	private String statusCode;

	Status(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return this.statusCode;
	}
	
	public static Optional<Status> fromStatusCode(String statusCode){
		for(Status s : Status.values()){
			if(s.getStatusCode().equals(statusCode)){
				return Optional.of(s);
			}
		}
		
		return Optional.empty();
	}
}
