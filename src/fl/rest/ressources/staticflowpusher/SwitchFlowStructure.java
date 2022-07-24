/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.staticflowpusher;

import fl.rest.ressources.OpenFlowStats.flows.FlowInstructiondSets;
import fl.rest.ressources.OpenFlowStats.flows.Match;
import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class SwitchFlowStructure {

    private FlowInstructiondSets instructions;
    private Match match;
    private String cookie = null;
    private String cookie_mask = null;
    private long hardTimeoutSec = 122;
    private long idleTimeoutSec = 1222;
    private Integer priority = 222;
    private String command = null;
    private String version;
    private String outPort = null;
    private String outGroup = null;
    private String flags = null;

    public SwitchFlowStructure(FlowInstructiondSets instructions, Match match, String version) {
        this.instructions = instructions;
        this.match = match;
        this.version = version;
    }

    public FlowInstructiondSets getInstructions() {
        return instructions;
    }

    public void setInstructions(FlowInstructiondSets instructions) {
        this.instructions = instructions;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie_mask() {
        return cookie_mask;
    }

    public void setCookie_mask(String cookie_mask) {
        this.cookie_mask = cookie_mask;
    }

    public long getHardTimeoutSec() {
        return hardTimeoutSec;
    }

    public void setHardTimeoutSec(long hardTimeoutSec) {
        this.hardTimeoutSec = hardTimeoutSec;
    }

    public long getIdleTimeoutSec() {
        return idleTimeoutSec;
    }

    public void setIdleTimeoutSec(long idleTimeoutSec) {
        this.idleTimeoutSec = idleTimeoutSec;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOutPort() {
        return outPort;
    }

    public void setOutPort(String outPort) {
        this.outPort = outPort;
    }

    public String getOutGroup() {
        return outGroup;
    }

    public void setOutGroup(String outGroup) {
        this.outGroup = outGroup;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.instructions);
        hash = 11 * hash + Objects.hashCode(this.match);
        hash = 11 * hash + Objects.hashCode(this.cookie);
        hash = 11 * hash + Objects.hashCode(this.cookie_mask);
        hash = 11 * hash + (int) (this.hardTimeoutSec ^ (this.hardTimeoutSec >>> 32));
        hash = 11 * hash + (int) (this.idleTimeoutSec ^ (this.idleTimeoutSec >>> 32));
        hash = 11 * hash + Objects.hashCode(this.priority);
        hash = 11 * hash + Objects.hashCode(this.command);
        hash = 11 * hash + Objects.hashCode(this.version);
        hash = 11 * hash + Objects.hashCode(this.outPort);
        hash = 11 * hash + Objects.hashCode(this.outGroup);
        hash = 11 * hash + Objects.hashCode(this.flags);
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
        final SwitchFlowStructure other = (SwitchFlowStructure) obj;
        if (this.hardTimeoutSec != other.hardTimeoutSec) {
            return false;
        }
        if (this.idleTimeoutSec != other.idleTimeoutSec) {
            return false;
        }
        if (!Objects.equals(this.cookie, other.cookie)) {
            return false;
        }
        if (!Objects.equals(this.cookie_mask, other.cookie_mask)) {
            return false;
        }
        if (!Objects.equals(this.command, other.command)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.flags, other.flags)) {
            return false;
        }
        if (!Objects.equals(this.instructions, other.instructions)) {
            return false;
        }
        if (!Objects.equals(this.match, other.match)) {
            return false;
        }
        if (!Objects.equals(this.priority, other.priority)) {
            return false;
        }
        if (!Objects.equals(this.outPort, other.outPort)) {
            return false;
        }
        if (!Objects.equals(this.outGroup, other.outGroup)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SwitchFlowStructure{" + "instructions=" + instructions + ", match=" + match + ", cookie=" + cookie + ", cookie_mask=" + cookie_mask + ", hardTimeoutSec=" + hardTimeoutSec + ", idleTimeoutSec=" + idleTimeoutSec + ", priority=" + priority + ", command=" + command + ", version=" + version + ", outPort=" + outPort + ", outGroup=" + outGroup + ", flags=" + flags + '}';
    }
    
    

}
