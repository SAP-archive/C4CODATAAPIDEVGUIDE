package main.java.venkyvb.odata.ticket;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Note
{

	private java.lang.String _author;

	private NoteType _noteType;

	private String _noteDescription;

	private Date _createdAt;
	
	public java.lang.String getAuthor()
	{
		return _author;
	}
	
	public NoteType getNoteType()
	{
		return _noteType;
	}
	
	public java.lang.String getNoteDescription()
	{
		return _noteDescription;
	}
	
	public java.util.Date getCreatedAt()
	{
		return _createdAt;
	}



	public void setAuthor(final java.lang.String _author)
	{
		this._author = _author;
	}

	public void setNoteType(final NoteType _noteType)
	{
		this._noteType = _noteType;
	}

	public void setNoteDescription(final java.lang.String _noteDescription)
	{
		this._noteDescription = _noteDescription;
	}

	public void setCreatedAt(final java.util.Date _createdAt)
	{
		this._createdAt = _createdAt;
	}

	@Override
	public String toString() {
		return "Note [_author=" + _author + ", _noteType=" + _noteType
				+ ", _noteDescription=" + _noteDescription + ", _createdAt="
				+ _createdAt + "]";
	}
}
