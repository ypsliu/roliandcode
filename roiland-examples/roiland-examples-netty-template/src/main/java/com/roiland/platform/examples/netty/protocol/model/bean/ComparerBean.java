package com.roiland.platform.examples.netty.protocol.model.bean;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/12
 */
public class ComparerBean {

    private String uuid;
    private String next;
    private String name;
    private String to;
    private String description;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ComparerBean [" +
                "uuid='" + uuid + '\'' +
                (next == null? "": ", next='" + next + '\'') +
                ", name='" + name + '\'' +
                ", to='" + to + '\'' +
                (description == null? "": ", description='" + description + '\'') +
                ']';
    }
}
