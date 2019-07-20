package com.roiland.platform.examples.netty.protocol.behaviour.obd;

import com.roiland.platform.examples.netty.protocol.IBehaviour;

import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 杨昆
 * @since 16/8/8
 */
public abstract class TreeNode implements IBehaviour<List<Map<String, Object>>> {

    protected TreeNode next = null;

    public void setNext(TreeNode next) {
        if (this.next == null) {
            this.next = next;
        } else {
            this.next.setNext(next);
        }
    }
}
