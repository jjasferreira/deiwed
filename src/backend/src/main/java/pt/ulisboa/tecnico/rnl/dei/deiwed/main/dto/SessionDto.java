package pt.ulisboa.tecnico.rnl.dei.deiwed.main.dto;

import java.io.Serializable;

import pt.ulisboa.tecnico.rnl.dei.deiwed.main.entity.Session;

// Data Transfer Object, to communicate with frontend
public class SessionDto implements Serializable {
    private long id;
    private String subject;
    private String speaker;
    private String date;
    private Integer normalDishId;
    private Integer vegetarianDishId;
    private Integer[] participantsIds;

    public SessionDto() {
    }

    public SessionDto(long id, String subject, String speaker,
            String date, Integer normalDishId, Integer vegetarianDishId, Integer[] participantsIds) {
        this.id = id;
        this.subject = subject;
        this.speaker = speaker;
        this.date = date;
        this.normalDishId = normalDishId;
        this.vegetarianDishId = vegetarianDishId;
        this.participantsIds = participantsIds;
    }

    public SessionDto(Session session) {
        this(session.getId(), session.getSubject(), session.getSpeaker(), session.getDate(),
                session.getNormalDishId(), session.getVegetarianDishId(), session.getParticipantsIds());
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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
                "SessionDto[id=%d, subject='%s', speaker='%s', date='%s', normalDishId='%d', vegetarianDishId='%d', participantsIds='%d']",
                id, subject, speaker, date, normalDishId, vegetarianDishId, participantsIds);
    }
}
