package main.java.venkyvb.odata.ticket;

import java.util.Optional;

public enum IssuePriority{

	VERY_HIGH("1"),HIGH("2"),NORMAL("3"),LOW("7");
	
	private final String priorityCode;
	
	IssuePriority(String priorityCode){
		this.priorityCode = priorityCode;
	}
	
	public String getPriorityCode(){
		return this.priorityCode;
	}
	
	public static Optional<IssuePriority> fromPriorityCode(String priorityCode){
		for(IssuePriority p : IssuePriority.values()){
			if(p.getPriorityCode().equals(priorityCode)){
				return Optional.of(p);
			}
		}
		
		return Optional.empty();
	}	
}
