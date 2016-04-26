package c4c.odata;

public class Attachment {
	private String _TypeCode;
	private String _MimeType;
	private byte[] _Binary;
	private String _Name;
	private String _ParentObjectID;
	private String fileName;

	public String get_TypeCode() {
		return _TypeCode;
	}
	public void set_TypeCode(String _TypeCode) {
		this._TypeCode = _TypeCode;
	}
	public String get_MimeType() {
		return _MimeType;
	}
	public void set_MimeType(String _MimeType) {
		this._MimeType = _MimeType;
	}
	public byte[] get_Binary() {
		return _Binary;
	}
	public void set_Binary(byte[] _Binary) {
		this._Binary = _Binary;
	}
	public String get_Name() {
		return _Name;
	}
	public void set_Name(String _Name) {
		this._Name = _Name;
	}
	public String get_ParentObjectID() {
		return _ParentObjectID;
	}
	public void set_ParentObjectID(String _ParentObjectID) {
		this._ParentObjectID = _ParentObjectID;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(", _TypeCode=");
		builder.append(_TypeCode);
		builder.append(", _MimeType=");
		builder.append(_MimeType);
		builder.append(", _Binary=");
		builder.append("***"); //	builder.append(_Binary);
		builder.append(", _Name=");
		builder.append(_Name);
		builder.append(", _ParentObjectID=");
		builder.append(_ParentObjectID);
		builder.append(", _RecordNumber=");
		builder.append("]");
		return builder.toString();
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}	
}
