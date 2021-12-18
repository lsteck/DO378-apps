package org.acme.conference.speaker;

import java.util.UUID;

public class Speaker {
    public UUID uuid;

    public String nameFirst;
    public String nameLast;
    public String organization;
    public String biography;
    public String picture;
    public String twitterHandle;

    public Speaker() {
    }

    public Speaker(String nameFirst, String nameLast, String organization, String biography, String picture,
            String twitterHandle, UUID uuid) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.organization = organization;
        this.biography = biography;
        this.picture = picture;
        this.twitterHandle = twitterHandle;

        this.uuid = uuid;
    }

    public Speaker(String nameFirst, String nameLast, String organization, String picture, String twitterHandle) {
        this(nameFirst, nameLast, organization, "Empty biography", picture, twitterHandle, UUID.randomUUID());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBiography() {
        return biography;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append("[");
        sb.append("<").append(id).append(">");
        sb.append(",");
        sb.append("nameFirst=").append(nameFirst);
        sb.append(",");
        sb.append("nameLast=").append(nameLast);
        sb.append("]");
        return sb.toString();
    }

}