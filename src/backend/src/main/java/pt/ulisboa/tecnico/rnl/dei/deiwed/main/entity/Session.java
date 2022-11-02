package pt.ulisboa.tecnico.rnl.dei.deiwed.main.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import java.sql.Date;

import pt.ulisboa.tecnico.rnl.dei.deiwed.main.dto.SessionDto;

@Entity
@Table(name = "sessions")
public class Session {

	@Id
	@GeneratedValue
	private Long id;

    @Column(name = "poster")
	private byte[] poster;

	@Column(name = "subject", nullable = false)
	private String subject;

	@Column(name = "speaker", nullable = false)
	private String speaker;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date", nullable = false)
	private Date date;

    @Column(name = "normal_dish", nullable = false)
	private String normalDish;

    @Column(name = "vegetarian_dish", nullable = false)
    private String vegetarianDish;

    @Column(name = "participants")
    private String[] participants;

	protected Session() {
	}

	public Session(byte[] poster, String subject, String speaker, Date date,
        String normalDish, String vegetarianDish, String[] participants) {
		this.poster = poster;
		this.subject = subject;
		this.speaker = speaker;
        this.date = date;
        this.normalDish = normalDish;
        this.vegetarianDish = vegetarianDish;
        this.participants = participants;
	}

	public Session(SessionDto sessionDto) {
		this(sessionDto.getPoster(), sessionDto.getSubject(), sessionDto.getSpeaker(),
        sessionDto.getDate(), sessionDto.getNormalDish(), sessionDto.getVegetarianDish(),
        sessionDto.getParticipants());
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
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
		return String.format(
				"Session[id=%d, poster='%s', subject='%s', speaker='%s', date='%s', normalDish='%s', vegetarianDish='%s', participants='%s']",
				id, poster, subject, speaker, date, normalDish, vegetarianDish, participants);
	}
}
