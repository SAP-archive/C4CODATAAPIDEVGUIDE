package main.java.venkyvb.odata.ticket;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Ticket {

	private java.lang.String _ticketId;

	private java.lang.String _ticketUUID;

	private java.lang.String _name;

	private Status _status;

	private java.lang.String _customerNote;

	private IssuePriority _issuePriority;

	private String _issueCategory;

	private List<Note> _notes;

	private String _productId;

	private java.util.Date _createdAt;

	private java.util.Date _changedAt;

	public java.lang.String getTicketId() {
		return _ticketId;
	}

	public java.lang.String getTicketUUID() {
		return _ticketUUID;
	}

	public java.lang.String getName() {
		return _name;
	}

	public Status getStatus() {
		return _status;
	}

	public java.lang.String getCustomerNote() {
		return _customerNote;
	}

	public IssuePriority getIssuePriority() {
		return _issuePriority;
	}

	public String getIssueCategory() {
		return _issueCategory;
	}

	public List<Note> getNotes() {
		return _notes;
	}

	public String getProductId() {
		return _productId;
	}

	public java.util.Date getCreatedAt() {
		return _createdAt;
	}

	public java.util.Date getChangedAt() {
		return _changedAt;
	}

	public void setTicketId(final java.lang.String _ticketId) {
		this._ticketId = _ticketId;
	}

	public void setTicketUUID(final java.lang.String _ticketUUID) {
		this._ticketUUID = _ticketUUID;
	}

	public void setName(final java.lang.String _name) {
		this._name = _name;
	}

	public void setStatus(final Status _status) {
		this._status = _status;
	}

	public void setCustomerNote(final java.lang.String _customerNote) {
		this._customerNote = _customerNote;
	}

	public void setIssuePriority(final IssuePriority _issuePriority) {
		this._issuePriority = _issuePriority;
	}

	public void setIssueCategory(final String _issueCategory) {
		this._issueCategory = _issueCategory;
	}

	public void setNotes(final List<Note> _notes) {
		this._notes = _notes;
	}

	public void setProductId(final String _productId) {
		this._productId = _productId;
	}

	public void setCreatedAt(final java.util.Date _createdAt) {
		this._createdAt = _createdAt;
	}

	public void setChangedAt(final java.util.Date _changedAt) {
		this._changedAt = _changedAt;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		if (_notes != null) {
			for (Note n : _notes) {
				sb.append(n.toString());
			}
		}

		return "Ticket [_ticketId=" + _ticketId + ", _ticketUUID="
				+ _ticketUUID + ", _name=" + _name + ", _status="
				+ _status + ", _customerNote=" + _customerNote
				+ ", _issuePriority=" + _issuePriority + ", _issueCategory="
				+ _issueCategory + ", _notes=" + sb.toString()
				+ ", _productId=" + _productId + ", _createdAt=" + _createdAt
				+ ", _changedAt=" + _changedAt + "]";
	}

}
