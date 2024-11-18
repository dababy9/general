package si413.spl;

public class SPLError extends RuntimeException {
    private static final long serialVersionUID = -1720828310974807346L;

    public SPLError(String message) {
        super(message);
    }
}
