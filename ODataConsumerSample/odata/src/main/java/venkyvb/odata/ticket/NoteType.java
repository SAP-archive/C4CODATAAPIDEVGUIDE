package main.java.venkyvb.odata.ticket;

import java.util.Optional;

public enum NoteType{

	INC_DESC("10004"),CUST_NOTE("10008"),INT_NOTE("10011"),AGENT_NOTE("10007");
	
	private String typeCode;
	
	NoteType(String typeCode){
		this.typeCode = typeCode;
	}
	
	public String getTypeCode(){
		return this.typeCode;
	}
	
	public static Optional<NoteType> fromTypeCode(String typeCode){
		for(NoteType n : NoteType.values()){
			if(n.getTypeCode().equals(typeCode)){
				return Optional.of(n);
			}
		}
		
		return Optional.empty();
	}	
}
