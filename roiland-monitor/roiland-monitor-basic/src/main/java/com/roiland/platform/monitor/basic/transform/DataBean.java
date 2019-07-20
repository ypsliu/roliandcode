package com.roiland.platform.monitor.basic.transform;

public class DataBean {

    private Integer success;
    private Integer failure;
    private Integer input;
    private Integer output;
    private Integer elapsed;
    private Integer concurrent;

    public DataBean(Integer success, Integer failure, Integer input, Integer output, Integer elapsed, Integer concurrent) {
        this.success = success;
        this.failure = failure;
        this.input = input;
        this.output = output;
        this.elapsed = elapsed;
        this.concurrent = concurrent;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public Integer getInput() {
        return input;
    }

    public void setInput(Integer input) {
        this.input = input;
    }

    public Integer getOutput() {
        return output;
    }

    public void setOutput(Integer output) {
        this.output = output;
    }

    public Integer getElapsed() {
        return elapsed;
    }

    public void setElapsed(Integer elapsed) {
        this.elapsed = elapsed;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Integer concurrent) {
        this.concurrent = concurrent;
    }
}
