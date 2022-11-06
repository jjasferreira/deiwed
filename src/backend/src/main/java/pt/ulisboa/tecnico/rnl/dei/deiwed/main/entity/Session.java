package pt.ulisboa.tecnico.rnl.dei.deiwed.main.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import pt.ulisboa.tecnico.rnl.dei.deiwed.main.dto.SessionDto;

@Entity
@Table(name = "sessions")
public class Session {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "subject", nullable = false)
	private String subject;

	@Column(name = "speaker", nullable = false)
	private String speaker;

	@Column(name = "date", nullable = false)
	private String date;

	@Column(name = "normal_dish_id", nullable = false)
	private Integer normalDishId;

	@Column(name = "vegetarian_dish_id", nullable = false)
	private Integer vegetarianDishId;

	@Column(name = "participants_ids")
	private Integer[] participantsIds;

	protected Session() {
	}

	public Session(String subject, String speaker, String date,
			Integer normalDishId, Integer vegetarianDishId, Integer[] participantsIds) {
		this.subject = subject;
		this.speaker = speaker;
		this.date = date;
		this.normalDishId = normalDishId;
		this.vegetarianDishId = vegetarianDishId;
		this.participantsIds = participantsIds;
	}

	public Session(SessionDto sessionDto) {
		this(sessionDto.getSubject(), sessionDto.getSpeaker(), sessionDto.getDate(),
				sessionDto.getNormalDishId(), sessionDto.getVegetarianDishId(), sessionDto.getParticipantsIds());
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getNormalDishId() {
		return this.normalDishId;
	}

	public void setNormalDishId(Integer normalDishId) {
		this.normalDishId = normalDishId;
	}

	public Integer getVegetarianDishId() {
		return this.vegetarianDishId;
	}

	public void setVegetarianDishId(Integer vegetarianDishId) {
		this.vegetarianDishId = vegetarianDishId;
	}

	public Integer[] getParticipantsIds() {
		return this.participantsIds;
	}

	public void setParticipantsIds(Integer[] participantsIds) {
		this.participantsIds = participantsIds;
	}

	@Override
	public String toString() {
		return String.format(
				"Session[id=%d, subject='%s', speaker='%s', date='%s', normalDishId='%d', vegetarianDishId='%d', participantsIds='%d']",
				id, subject, speaker, date, normalDishId, vegetarianDishId, participantsIds);
	}
}
