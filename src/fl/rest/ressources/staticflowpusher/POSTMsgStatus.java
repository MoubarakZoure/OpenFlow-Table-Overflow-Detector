/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.staticflowpusher;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class POSTMsgStatus {

    private String status;

    public POSTMsgStatus() {
    }

    @Override
    public String toString() {
        return "POSTMsgStatus{" + "status=" + status + '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public POSTMsgStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.status);
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
        final POSTMsgStatus other = (POSTMsgStatus) obj;
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }

}
