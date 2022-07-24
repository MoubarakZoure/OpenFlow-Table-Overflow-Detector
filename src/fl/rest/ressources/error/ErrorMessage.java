/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.error;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class ErrorMessage {

    private boolean clientError;
    private boolean code;
    private boolean connectorError;
    private boolean description;
    private boolean error;
    private boolean globalError;
    private boolean informational;
    private String reasonPhrase;
    private boolean recoverableError;
    private boolean redirection;
    private boolean serverError;
    private boolean success;
    private String throwable;
    private String uri;

    public ErrorMessage(boolean clientError, boolean code, boolean connectorError, boolean description, boolean error, boolean globalError, boolean informational, String reasonPhrase, boolean recoverableError, boolean redirection, boolean serverError, boolean success, String throwable, String uri) {
        this.clientError = clientError;
        this.code = code;
        this.connectorError = connectorError;
        this.description = description;
        this.error = error;
        this.globalError = globalError;
        this.informational = informational;
        this.reasonPhrase = reasonPhrase;
        this.recoverableError = recoverableError;
        this.redirection = redirection;
        this.serverError = serverError;
        this.success = success;
        this.throwable = throwable;
        this.uri = uri;
    }

    public boolean isClientError() {
        return clientError;
    }

    public void setClientError(boolean clientError) {
        this.clientError = clientError;
    }

    public boolean isCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }

    public boolean isConnectorError() {
        return connectorError;
    }

    public void setConnectorError(boolean connectorError) {
        this.connectorError = connectorError;
    }

    public boolean isDescription() {
        return description;
    }

    public void setDescription(boolean description) {
        this.description = description;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isGlobalError() {
        return globalError;
    }

    public void setGlobalError(boolean globalError) {
        this.globalError = globalError;
    }

    public boolean isInformational() {
        return informational;
    }

    public void setInformational(boolean informational) {
        this.informational = informational;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public boolean isRecoverableError() {
        return recoverableError;
    }

    public void setRecoverableError(boolean recoverableError) {
        this.recoverableError = recoverableError;
    }

    public boolean isRedirection() {
        return redirection;
    }

    public void setRedirection(boolean redirection) {
        this.redirection = redirection;
    }

    public boolean isServerError() {
        return serverError;
    }

    public void setServerError(boolean serverError) {
        this.serverError = serverError;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getThrowable() {
        return throwable;
    }

    public void setThrowable(String throwable) {
        this.throwable = throwable;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" + "clientError=" + clientError + ", code=" + code + ", connectorError=" + connectorError + ", description=" + description + ", error=" + error + ", globalError=" + globalError + ", informational=" + informational + ", reasonPhrase=" + reasonPhrase + ", recoverableError=" + recoverableError + ", redirection=" + redirection + ", serverError=" + serverError + ", success=" + success + ", throwable=" + throwable + ", uri=" + uri + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.clientError ? 1 : 0);
        hash = 67 * hash + (this.code ? 1 : 0);
        hash = 67 * hash + (this.connectorError ? 1 : 0);
        hash = 67 * hash + (this.description ? 1 : 0);
        hash = 67 * hash + (this.error ? 1 : 0);
        hash = 67 * hash + (this.globalError ? 1 : 0);
        hash = 67 * hash + (this.informational ? 1 : 0);
        hash = 67 * hash + Objects.hashCode(this.reasonPhrase);
        hash = 67 * hash + (this.recoverableError ? 1 : 0);
        hash = 67 * hash + (this.redirection ? 1 : 0);
        hash = 67 * hash + (this.serverError ? 1 : 0);
        hash = 67 * hash + (this.success ? 1 : 0);
        hash = 67 * hash + Objects.hashCode(this.throwable);
        hash = 67 * hash + Objects.hashCode(this.uri);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ErrorMessage other = (ErrorMessage) obj;
        if (this.clientError != other.clientError) {
            return false;
        }
        if (this.code != other.code) {
            return false;
        }
        if (this.connectorError != other.connectorError) {
            return false;
        }
        if (this.description != other.description) {
            return false;
        }
        if (this.error != other.error) {
            return false;
        }
        if (this.globalError != other.globalError) {
            return false;
        }
        if (this.informational != other.informational) {
            return false;
        }
        if (this.recoverableError != other.recoverableError) {
            return false;
        }
        if (this.redirection != other.redirection) {
            return false;
        }
        if (this.serverError != other.serverError) {
            return false;
        }
        if (this.success != other.success) {
            return false;
        }
        if (!Objects.equals(this.reasonPhrase, other.reasonPhrase)) {
            return false;
        }
        if (!Objects.equals(this.throwable, other.throwable)) {
            return false;
        }
        if (!Objects.equals(this.uri, other.uri)) {
            return false;
        }
        return true;
    }
    
    

}
