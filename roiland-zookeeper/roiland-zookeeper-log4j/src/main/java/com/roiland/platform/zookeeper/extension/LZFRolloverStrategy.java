package com.roiland.platform.zookeeper.extension;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescription;
import org.apache.logging.log4j.core.appender.rolling.RolloverDescriptionImpl;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.CompositeAction;
import org.apache.logging.log4j.core.appender.rolling.action.FileRenameAction;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusLogger;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * lzf日志压缩，更好的性能以及更低的cpu利用率。
 * 解决RollingRandomAccessFileAppender中由于日志压缩引起的block问题
 * @author leon.chen
 * @since 2016/6/29
 */
@Plugin(name = "LZFRolloverStrategy", category = "Core", printObject = true)
public class LZFRolloverStrategy implements RolloverStrategy {

    static enum FileExtensions {
        LZF(".lzf") {
            @Override
            Action createCompressAction(final String renameTo, final String compressedName, final boolean deleteSource) {
                return new LZFCompressAction(source(renameTo), target(compressedName), deleteSource);
            }
        };

        private final String extension;

        private FileExtensions(final String extension) {
            Objects.requireNonNull(extension, "extension");
            this.extension = extension;
        }

        abstract Action createCompressAction(String renameTo, String compressedName, boolean deleteSource);

        boolean isExtensionFor(final String s) {
            return s.endsWith(this.extension);
        }

        int length() {
            return extension.length();
        }

        File source(final String fileName) {
            return new File(fileName);
        }

        File target(final String fileName) {
            return new File(fileName);
        }
    }

    /**
     * Allow subclasses access to the status logger without creating another instance.
     */
    protected static final Logger LOGGER = StatusLogger.getLogger();

    private static final int MIN_WINDOW_SIZE = 1;
    private static final int DEFAULT_WINDOW_SIZE = 7;

    @PluginFactory
    public static LZFRolloverStrategy createStrategy(
            // @formatter:off
            @PluginAttribute("max") final String max,
            @PluginAttribute("min") final String min,
            @PluginAttribute("fileIndex") final String fileIndex,
            @PluginElement("Actions") final Action[] customActions,
            @PluginAttribute(value = "stopCustomActionsOnError", defaultBoolean = true)
            final boolean stopCustomActionsOnError,
            @PluginConfiguration final Configuration config) {
        // @formatter:on
        final boolean useMax = fileIndex == null ? true : fileIndex.equalsIgnoreCase("max");
        int minIndex = MIN_WINDOW_SIZE;
        if (min != null) {
            minIndex = Integer.parseInt(min);
            if (minIndex < 1) {
                LOGGER.error("Minimum window size too small. Limited to " + MIN_WINDOW_SIZE);
                minIndex = MIN_WINDOW_SIZE;
            }
        }
        int maxIndex = DEFAULT_WINDOW_SIZE;
        if (max != null) {
            maxIndex = Integer.parseInt(max);
            if (maxIndex < minIndex) {
                maxIndex = minIndex < DEFAULT_WINDOW_SIZE ? DEFAULT_WINDOW_SIZE : minIndex;
                LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + maxIndex);
            }
        }
        return new LZFRolloverStrategy(minIndex, maxIndex, useMax, config.getStrSubstitutor(),
                customActions, stopCustomActionsOnError);
    }

    private final int maxIndex;
    private final int minIndex;
    private final boolean useMax;
    private final StrSubstitutor strSubstitutor;
    private final List<Action> customActions;
    private final boolean stopCustomActionsOnError;

    protected LZFRolloverStrategy(final int minIndex, final int maxIndex, final boolean useMax,
                                  final StrSubstitutor strSubstitutor, final Action[] customActions,
                                  final boolean stopCustomActionsOnError) {
        this.minIndex = minIndex;
        this.maxIndex = maxIndex;
        this.useMax = useMax;
        this.strSubstitutor = strSubstitutor;
        this.stopCustomActionsOnError = stopCustomActionsOnError;
        this.customActions = customActions == null ? Collections.<Action> emptyList() : Arrays.asList(customActions);
    }

    private Action merge(final Action compressAction, final List<Action> custom, final boolean stopOnError) {
        if (custom.isEmpty()) {
            return compressAction;
        }
        if (compressAction == null) {
            return new CompositeAction(custom, stopOnError);
        }
        final List<Action> all = new ArrayList<>();
        all.add(compressAction);
        all.addAll(custom);
        return new CompositeAction(all, stopOnError);
    }

    private int purge(final int lowIndex, final int highIndex, final RollingFileManager manager) {
        return useMax ? purgeAscending(lowIndex, highIndex, manager) : purgeDescending(lowIndex, highIndex, manager);
    }

    private int purgeAscending(final int lowIndex, final int highIndex, final RollingFileManager manager) {
        final List<FileRenameAction> renames = new ArrayList<>();
        final StringBuilder buf = new StringBuilder();

        // LOG4J2-531: directory scan & rollover must use same format
        manager.getPatternProcessor().formatFileName(strSubstitutor, buf, highIndex);
        String highFilename = strSubstitutor.replace(buf);
        final int suffixLength = suffixLength(highFilename);
        int curMaxIndex = 0;

        for (int i = highIndex; i >= lowIndex; i--) {
            File toRename = new File(highFilename);
            if (i == highIndex && toRename.exists()) {
                curMaxIndex = highIndex;
            } else if (curMaxIndex == 0 && toRename.exists()) {
                curMaxIndex = i + 1;
                break;
            }

            boolean isBase = false;

            if (suffixLength > 0) {
                final File toRenameBase = new File(highFilename.substring(0, highFilename.length() - suffixLength));

                if (toRename.exists()) {
                    if (toRenameBase.exists()) {
                        LOGGER.debug("LZ4RolloverStrategy.purgeAscending deleting {} base of {}.", //
                                toRenameBase, toRename);
                        toRenameBase.delete();
                    }
                } else {
                    toRename = toRenameBase;
                    isBase = true;
                }
            }

            if (toRename.exists()) {
                //
                // if at lower index and then all slots full
                // attempt to delete last file
                // if that fails then abandon purge
                if (i == lowIndex) {
                    LOGGER.debug("LZ4RolloverStrategy.purgeAscending deleting {} at low index {}: all slots full.",
                            toRename, i);
                    if (!toRename.delete()) {
                        return -1;
                    }

                    break;
                }

                //
                // if intermediate index
                // add a rename action to the list
                buf.setLength(0);
                // LOG4J2-531: directory scan & rollover must use same format
                manager.getPatternProcessor().formatFileName(strSubstitutor, buf, i - 1);

                final String lowFilename = strSubstitutor.replace(buf);
                String renameTo = lowFilename;

                if (isBase) {
                    renameTo = lowFilename.substring(0, lowFilename.length() - suffixLength);
                }

                renames.add(new FileRenameAction(toRename, new File(renameTo), true));
                highFilename = lowFilename;
            } else {
                buf.setLength(0);
                // LOG4J2-531: directory scan & rollover must use same format
                manager.getPatternProcessor().formatFileName(strSubstitutor, buf, i - 1);

                highFilename = strSubstitutor.replace(buf);
            }
        }
        if (curMaxIndex == 0) {
            curMaxIndex = lowIndex;
        }

        //
        // work renames backwards
        //
        for (int i = renames.size() - 1; i >= 0; i--) {
            final Action action = renames.get(i);
            try {
                LOGGER.debug("LZ4RolloverStrategy.purgeAscending executing {} of {}: {}", //
                        i, renames.size(), action);
                if (!action.execute()) {
                    return -1;
                }
            } catch (final Exception ex) {
                LOGGER.warn("Exception during purge in RollingFileAppender", ex);
                return -1;
            }
        }
        return curMaxIndex;
    }

    private int purgeDescending(final int lowIndex, final int highIndex, final RollingFileManager manager) {
        final List<FileRenameAction> renames = new ArrayList<>();
        final StringBuilder buf = new StringBuilder();

        // LOG4J2-531: directory scan & rollover must use same format
        manager.getPatternProcessor().formatFileName(strSubstitutor, buf, lowIndex);

        String lowFilename = strSubstitutor.replace(buf);
        final int suffixLength = suffixLength(lowFilename);

        for (int i = lowIndex; i <= highIndex; i++) {
            File toRename = new File(lowFilename);
            boolean isBase = false;

            if (suffixLength > 0) {
                final File toRenameBase = new File(lowFilename.substring(0, lowFilename.length() - suffixLength));

                if (toRename.exists()) {
                    if (toRenameBase.exists()) {
                        LOGGER.debug("LZ4RolloverStrategy.purgeDescending deleting {} base of {}.", //
                                toRenameBase, toRename);
                        toRenameBase.delete();
                    }
                } else {
                    toRename = toRenameBase;
                    isBase = true;
                }
            }

            if (toRename.exists()) {
                //
                // if at upper index then
                // attempt to delete last file
                // if that fails then abandon purge
                if (i == highIndex) {
                    LOGGER.debug(
                            "LZ4RolloverStrategy.purgeDescending deleting {} at high index {}: all slots full.", //
                            toRename, i);
                    if (!toRename.delete()) {
                        return -1;
                    }

                    break;
                }

                //
                // if intermediate index
                // add a rename action to the list
                buf.setLength(0);
                // LOG4J2-531: directory scan & rollover must use same format
                manager.getPatternProcessor().formatFileName(strSubstitutor, buf, i + 1);

                final String highFilename = strSubstitutor.replace(buf);
                String renameTo = highFilename;

                if (isBase) {
                    renameTo = highFilename.substring(0, highFilename.length() - suffixLength);
                }

                renames.add(new FileRenameAction(toRename, new File(renameTo), true));
                lowFilename = highFilename;
            } else {
                break;
            }
        }

        //
        // work renames backwards
        //
        for (int i = renames.size() - 1; i >= 0; i--) {
            final Action action = renames.get(i);
            try {
                LOGGER.debug("LZ4RolloverStrategy.purgeDescending executing {} of {}: {}", //
                        i, renames.size(), action);
                if (!action.execute()) {
                    return -1;
                }
            } catch (final Exception ex) {
                LOGGER.warn("Exception during purge in RollingFileAppender", ex);
                return -1;
            }
        }

        return lowIndex;
    }

    @Override
    public RolloverDescription rollover(final RollingFileManager manager) throws SecurityException {
        if (maxIndex < 0) {
            return null;
        }
        final long startNanos = System.nanoTime();
        final int fileIndex = purge(minIndex, maxIndex, manager);
        if (fileIndex < 0) {
            return null;
        }
        if (LOGGER.isTraceEnabled()) {
            final double durationMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
            LOGGER.trace("LZ4RolloverStrategy.purge() took {} milliseconds", durationMillis);
        }
        final StringBuilder buf = new StringBuilder(255);
        manager.getPatternProcessor().formatFileName(strSubstitutor, buf, fileIndex);
        final String currentFileName = manager.getFileName();

        String renameTo = buf.toString();
        final String compressedName = renameTo;
        Action compressAction = null;

        for (final FileExtensions ext : FileExtensions.values()) { // LOG4J2-1077 support other compression formats
            if (ext.isExtensionFor(renameTo)) {
                renameTo = renameTo.substring(0, renameTo.length() - ext.length()); // LOG4J2-1135 omit extension!
                compressAction = ext.createCompressAction(renameTo, compressedName, true);
                break;
            }
        }

        final FileRenameAction renameAction = new FileRenameAction(new File(currentFileName), new File(renameTo), false);

        final Action asyncAction = merge(compressAction, customActions, stopCustomActionsOnError);
        return new RolloverDescriptionImpl(currentFileName, false, renameAction, asyncAction);
    }

    private int suffixLength(final String lowFilename) {
        for (final FileExtensions extension : FileExtensions.values()) {
            if (extension.isExtensionFor(lowFilename)) {
                return extension.length();
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "LZ4RolloverStrategy(min=" + minIndex + ", max=" + maxIndex + ')';
    }

}
