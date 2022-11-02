package pt.ulisboa.tecnico.rnl.dei.deiwed.main.dto;

import java.io.Serializable;

import java.sql.Date;

import pt.ulisboa.tecnico.rnl.dei.deiwed.main.entity.Session;

// Data Transfer Object, to communicate with frontend
public class SessionDto implements Serializable {
    private long id;
	private byte[] poster;
    private String subject;
    private String speaker;
    private Date date;
    private String normalDish;
    private String vegetarianDish;
    private String[] participants;

	public SessionDto() {
	}

	public SessionDto(long id, byte[] poster, String subject, String speaker,
    Date date, String normalDish, String vegetarianDish, String[] participants) {
        this.id = id;
		this.poster = poster;
		this.subject = subject;
		this.speaker = speaker;
        this.date = date;
        this.normalDish = normalDish;
        this.vegetarianDish = vegetarianDish;
        this.participants = participants;
	}

	public SessionDto(Session session) {
		this(session.getId(), session.getPoster(), session.getSubject(),
        session.getSpeaker(), session.getDate(), session.getNormalDish(),
        session.getVegetarianDish(), session.getParticipants());
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getPoster() {
		return this.poster;
	}

	public void setPoster(byte[] poster) {
		this.poster = poster;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSpeaker() {
		return this.speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNormalDish() {
        return this.normalDish;
    }

    public void setNormalDish(String normalDish) {
        this.normalDish = normalDish;
    }

    public String getVegetarianDish() {
        return this.vegetarianDish;
    }

    public void setVegetarianDish(String vegetarianDish) {
        this.vegetarianDish = vegetarianDish;
    }

    public String[] getParticipants() {
        return this.participants;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants;
    }

	@Override
	public String toString() {
		return String.format("SessionDto[id=%d, poster='%s', subject='%s', speaker='%s', date='%s', normalDish='%s', vegetarianDish='%s', participants='%s']",
        id, poster, subject, speaker, date, normalDish, vegetarianDish, participants);
	}
}
