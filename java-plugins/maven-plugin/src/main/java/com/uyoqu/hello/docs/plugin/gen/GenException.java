package com.uyoqu.hello.docs.plugin.gen;

/**
 * GEN异常。
 * Created by zhpeng2 on 2017/9/29.
 */
public class GenException extends Exception {
    private Class<?> clazz;

    public Class<?> getClazz() {
        return clazz;
    }

    private void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public GenException() {
        super();
    }

    public GenException(Class<?> clazz) {
        super((clazz != null ? clazz.getCanonicalName() : ""));
        setClazz(clazz);
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public GenException(String message) {
        super(message);
    }

    public GenException(String message, Class<?> clazz) {
        super(message + (clazz != null ? clazz.getCanonicalName() : ""));
        setClazz(clazz);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public GenException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenException(String message, Class<?> clazz, Throwable cause) {
        super(message + (clazz != null ? clazz.getCanonicalName() : ""), cause);
        setClazz(clazz);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public GenException(Throwable cause) {
        super(cause);
    }

}
