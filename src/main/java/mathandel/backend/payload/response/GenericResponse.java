package mathandel.backend.payload.response;

public class GenericResponse {

    private Boolean isSuccess;
    private String message;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean successfull) {
        isSuccess = successfull;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
