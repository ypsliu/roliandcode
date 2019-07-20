package com.roiland.platform.examples.netty.protocol.model.bean;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class OBDBean {

    private String uuid;
    private String command;
    private String description;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OBDBean [" +
                "uuid='" + uuid + '\'' +
                ", command='" + command + '\'' +
                (description == null? "": ", description='" + description + "\'") + "]";
    }
}
