package app.domain.exceptions;

public class UnauthorizedClientAccessException extends RuntimeException {
    public UnauthorizedClientAccessException(String employeeId, String clientId) {
        super("Empleado " + employeeId + " no está autorizado para gestionar cliente " + clientId);
    }
}
