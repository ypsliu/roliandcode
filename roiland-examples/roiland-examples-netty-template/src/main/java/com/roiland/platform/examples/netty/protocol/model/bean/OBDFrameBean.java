package com.roiland.platform.examples.netty.protocol.model.bean;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/5
 */
public class OBDFrameBean {

    private String command;
    private String frame;
    private String parent;
    private Integer position;
    private String description;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OBDFrameBean [command='" + command + '\'' +
                ", frame='" + frame + '\'' +
                (parent == null? "": ", parent='" + parent + '\'') +
                ", position=" + position +
                (description == null? "": ", description='" + description + '\'') + "]";
    }
}
