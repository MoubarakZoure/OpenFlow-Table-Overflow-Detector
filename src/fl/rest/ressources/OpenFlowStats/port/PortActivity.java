/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.port;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class PortActivity {

    private long collisions;
    private long durationNsec;
    private long durationSec;
    private String  portNumber;
    private long receiveBytes;
    private long receiveCRCErrors;
    private long receiveDropped;
    private long receiveErrors;
    private long receiveFrameErrors;
    private long receiveOverrunErrors;
    private long receivePackets;
    private long transmitBytes;
    private long transmitDropped;
    private long transmitErrors;
    private long transmitPackets;
    
    

    public PortActivity(long collisions, long durationNsec, long durationSec, String  portNumber, long receiveBytes, long receiveCRCErrors, long receiveDropped, long receiveErrors, long receiveFrameErrors, long receiveOverrunErrors, long receivePackets, long transmitBytes, long transmitDropped, long transmitErrors, long transmitPackets) {
        this.collisions = collisions;
        this.durationNsec = durationNsec;
        this.durationSec = durationSec;
        this.portNumber = portNumber;
        this.receiveBytes = receiveBytes;
        this.receiveCRCErrors = receiveCRCErrors;
        this.receiveDropped = receiveDropped;
        this.receiveErrors = receiveErrors;
        this.receiveFrameErrors = receiveFrameErrors;
        this.receiveOverrunErrors = receiveOverrunErrors;
        this.receivePackets = receivePackets;
        this.transmitBytes = transmitBytes;
        this.transmitDropped = transmitDropped;
        this.transmitErrors = transmitErrors;
        this.transmitPackets = transmitPackets;
    }

    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (this.collisions ^ (this.collisions >>> 32));
        hash = 37 * hash + (int) (this.durationNsec ^ (this.durationNsec >>> 32));
        hash = 37 * hash + (int) (this.durationSec ^ (this.durationSec >>> 32));
        hash = 37 * hash + Objects.hashCode(this.portNumber);
        hash = 37 * hash + (int) (this.receiveBytes ^ (this.receiveBytes >>> 32));
        hash = 37 * hash + (int) (this.receiveCRCErrors ^ (this.receiveCRCErrors >>> 32));
        hash = 37 * hash + (int) (this.receiveDropped ^ (this.receiveDropped >>> 32));
        hash = 37 * hash + (int) (this.receiveErrors ^ (this.receiveErrors >>> 32));
        hash = 37 * hash + (int) (this.receiveFrameErrors ^ (this.receiveFrameErrors >>> 32));
        hash = 37 * hash + (int) (this.receiveOverrunErrors ^ (this.receiveOverrunErrors >>> 32));
        hash = 37 * hash + (int) (this.receivePackets ^ (this.receivePackets >>> 32));
        hash = 37 * hash + (int) (this.transmitBytes ^ (this.transmitBytes >>> 32));
        hash = 37 * hash + (int) (this.transmitDropped ^ (this.transmitDropped >>> 32));
        hash = 37 * hash + (int) (this.transmitErrors ^ (this.transmitErrors >>> 32));
        hash = 37 * hash + (int) (this.transmitPackets ^ (this.transmitPackets >>> 32));
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
        final PortActivity other = (PortActivity) obj;
        if (this.collisions != other.collisions) {
            return false;
        }
        if (this.durationNsec != other.durationNsec) {
            return false;
        }
        if (this.durationSec != other.durationSec) {
            return false;
        }
        if (this.receiveBytes != other.receiveBytes) {
            return false;
        }
        if (this.receiveCRCErrors != other.receiveCRCErrors) {
            return false;
        }
        if (this.receiveDropped != other.receiveDropped) {
            return false;
        }
        if (this.receiveErrors != other.receiveErrors) {
            return false;
        }
        if (this.receiveFrameErrors != other.receiveFrameErrors) {
            return false;
        }
        if (this.receiveOverrunErrors != other.receiveOverrunErrors) {
            return false;
        }
        if (this.receivePackets != other.receivePackets) {
            return false;
        }
        if (this.transmitBytes != other.transmitBytes) {
            return false;
        }
        if (this.transmitDropped != other.transmitDropped) {
            return false;
        }
        if (this.transmitErrors != other.transmitErrors) {
            return false;
        }
        if (this.transmitPackets != other.transmitPackets) {
            return false;
        }
        if (!Objects.equals(this.portNumber, other.portNumber)) {
            return false;
        }
        return true;
    }

    

   

    public long getCollisions() {
        return collisions;
    }

    public void setCollisions(long collisions) {
        this.collisions = collisions;
    }

    public long getDurationNsec() {
        return durationNsec;
    }

    public void setDurationNsec(long durationNsec) {
        this.durationNsec = durationNsec;
    }

    public long getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(long durationSec) {
        this.durationSec = durationSec;
    }

    public String  getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String  portNumber) {
        this.portNumber = portNumber;
    }

    public long getReceiveBytes() {
        return receiveBytes;
    }

    public void setReceiveBytes(long receiveBytes) {
        this.receiveBytes = receiveBytes;
    }

    public long getReceiveCRCErrors() {
        return receiveCRCErrors;
    }

    public void setReceiveCRCErrors(long receiveCRCErrors) {
        this.receiveCRCErrors = receiveCRCErrors;
    }

    public long getReceiveDropped() {
        return receiveDropped;
    }

    public void setReceiveDropped(long receiveDropped) {
        this.receiveDropped = receiveDropped;
    }

    public long getReceiveErrors() {
        return receiveErrors;
    }

    public void setReceiveErrors(long receiveErrors) {
        this.receiveErrors = receiveErrors;
    }

    public long getReceiveFrameErrors() {
        return receiveFrameErrors;
    }

    public void setReceiveFrameErrors(long receiveFrameErrors) {
        this.receiveFrameErrors = receiveFrameErrors;
    }

    public long getReceiveOverrunErrors() {
        return receiveOverrunErrors;
    }

    public void setReceiveOverrunErrors(long receiveOverrunErrors) {
        this.receiveOverrunErrors = receiveOverrunErrors;
    }

    public long getReceivePackets() {
        return receivePackets;
    }

    public void setReceivePackets(long receivePackets) {
        this.receivePackets = receivePackets;
    }

    public long getTransmitBytes() {
        return transmitBytes;
    }

    public void setTransmitBytes(long transmitBytes) {
        this.transmitBytes = transmitBytes;
    }

    public long getTransmitDropped() {
        return transmitDropped;
    }

    public void setTransmitDropped(long transmitDropped) {
        this.transmitDropped = transmitDropped;
    }

    public long getTransmitErrors() {
        return transmitErrors;
    }

    public void setTransmitErrors(long transmitErrors) {
        this.transmitErrors = transmitErrors;
    }

    public long getTransmitPackets() {
        return transmitPackets;
    }

    public void setTransmitPackets(long transmitPackets) {
        this.transmitPackets = transmitPackets;
    }

    @Override
    public String toString() {
        return "PortActivity{" + "collisions=" + collisions + ", durationNsec=" + durationNsec + ", durationSec=" + durationSec + ", portNumber=" + portNumber + ", receiveBytes=" + receiveBytes + ", receiveCRCErrors=" + receiveCRCErrors + ", receiveDropped=" + receiveDropped + ", receiveErrors=" + receiveErrors + ", receiveFrameErrors=" + receiveFrameErrors + ", receiveOverrunErrors=" + receiveOverrunErrors + ", receivePackets=" + receivePackets + ", transmitBytes=" + transmitBytes + ", transmitDropped=" + transmitDropped + ", transmitErrors=" + transmitErrors + ", transmitPackets=" + transmitPackets + '}';
    }

}
