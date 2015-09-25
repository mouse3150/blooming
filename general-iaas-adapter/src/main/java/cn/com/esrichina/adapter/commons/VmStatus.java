package cn.com.esrichina.adapter.commons;

public enum VmStatus {
    /**
     * The server is in an error state, and unlikely to be operational.
     */
    ERROR,
    /**
     * The virtual machine is in a transitional state between known states, generally from {@link #STOPPED} to {@link #RUNNING}.
     */
    PENDING,
    /**
     * The server is fully operational as far as we know.
     */
    RUNNING,
    /**
     * The server is currently in the middle of a reboot and should be operational shortly.
     */
    REBOOTING,
    /**
     * The virtual machine is currently in a stopped state with no current state information stored anywhere.
     */
    STOPPED,
    /**
     * The server is currently being stopped.
     */
    STOPPING,
    /**
     * The virtual machine is currently suspended to disk and otherwise non-operational.
     */
    SUSPENDED,
    /**
     * The virtual machine is in the process of being suspended to disk.
     */
    SUSPENDING,
    /**
     * The server has been terminated and is thus entirely useless and all state information is lost.
     */
    TERMINATED,
    /**
     * The server is currently being unknown status.
     */
    UNKNOWN
}