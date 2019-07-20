package com.roiland.platform.zookeeper.extension;

import com.ning.compress.lzf.util.LZFFileOutputStream;
import org.apache.logging.log4j.core.appender.rolling.action.AbstractAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author leon.chen
 * @since 2016/6/29
 */
public class LZFCompressAction extends AbstractAction {
    private static final int BUF_SIZE = 8102;
    private final File source;
    private final File destination;
    private final boolean deleteSource;

    public LZFCompressAction(final File source, final File destination, final boolean deleteSource) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        this.source = source;
        this.destination = destination;
        this.deleteSource = deleteSource;
    }

    @Override
    public boolean execute() throws IOException {
        return execute(source, destination, deleteSource);
    }

    public static boolean execute(final File source, final File destination, final boolean deleteSource) throws IOException {
        if (source.exists()) {
            try (final FileInputStream fis = new FileInputStream(source);
                 final LZFFileOutputStream los = new LZFFileOutputStream(destination)){
                final byte[] inbuf = new byte[BUF_SIZE];
                int n;

                while ((n = fis.read(inbuf)) != -1) {
                    los.write(inbuf, 0, n);
                }
            }
            if (deleteSource && !source.delete()) {
                LOGGER.warn("Unable to delete " + source.toString() + '.');
            }

            return true;
        }

        return false;
    }

    @Override
    protected void reportException(final Exception ex) {
        LOGGER.warn("Exception during compression of '" + source.toString() + "'.", ex);
    }
}
