/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fl.rest.ressources.OpenFlowStats.flows;

import java.util.Objects;

/**
 *
 * @author Moubarak
 */
public class FlowInstructiondSets {

    private Instruction instruction_goto_table;
    private Instruction instruction_write_metadata;
    private Instruction instruction_write_actions;
    private Instruction instruction_apply_actions=new Instruction("output\"=controller");
    private Instruction instruction_clear_actions;
    private Instruction instruction_goto_meter;
    private Instruction instruction_experimenter;
    private String actions;

    public FlowInstructiondSets() {
    }
    
    

    public String getActions() {
        return actions;
    }

    public FlowInstructiondSets(Instruction instruction_goto_table, Instruction instruction_write_metadata, Instruction instruction_write_actions, Instruction instruction_apply_actions, Instruction instruction_clear_actions, Instruction instruction_goto_meter, Instruction instruction_experimenter, String actions) {
        this.instruction_goto_table = instruction_goto_table;
        this.instruction_write_metadata = instruction_write_metadata;
        this.instruction_write_actions = instruction_write_actions;
        this.instruction_apply_actions = instruction_apply_actions;
        this.instruction_clear_actions = instruction_clear_actions;
        this.instruction_goto_meter = instruction_goto_meter;
        this.instruction_experimenter = instruction_experimenter;
        this.actions = actions;
    }
    
    

    public void setActions(String actions) {
        this.actions = actions;
    }
    
    

    public FlowInstructiondSets(Instruction instruction_goto_table, Instruction instruction_write_metadata, Instruction instruction_write_actions, Instruction instruction_apply_actions, Instruction instruction_clear_actions, Instruction instruction_goto_meter, Instruction instruction_experimenter) {
        this.instruction_goto_table = instruction_goto_table;
        this.instruction_write_metadata = instruction_write_metadata;
        this.instruction_write_actions = instruction_write_actions;
        this.instruction_apply_actions = instruction_apply_actions;
        this.instruction_clear_actions = instruction_clear_actions;
        this.instruction_goto_meter = instruction_goto_meter;
        this.instruction_experimenter = instruction_experimenter;
    }

    public Instruction getInstruction_goto_table() {
        return instruction_goto_table;
    }

    public void setInstruction_goto_table(Instruction instruction_goto_table) {
        this.instruction_goto_table = instruction_goto_table;
    }

    public Instruction getInstruction_write_metadata() {
        return instruction_write_metadata;
    }

    public void setInstruction_write_metadata(Instruction instruction_write_metadata) {
        this.instruction_write_metadata = instruction_write_metadata;
    }

    public Instruction getInstruction_write_actions() {
        return instruction_write_actions;
    }

    public void setInstruction_write_actions(Instruction instruction_write_actions) {
        this.instruction_write_actions = instruction_write_actions;
    }

    public Instruction getInstruction_apply_actions() {
        return instruction_apply_actions;
    }

    public void setInstruction_apply_actions(Instruction instruction_apply_actions) {
        this.instruction_apply_actions = instruction_apply_actions;
    }

    public Instruction getInstruction_clear_actions() {
        return instruction_clear_actions;
    }

    public void setInstruction_clear_actions(Instruction instruction_clear_actions) {
        this.instruction_clear_actions = instruction_clear_actions;
    }

    public Instruction getInstruction_goto_meter() {
        return instruction_goto_meter;
    }

    public void setInstruction_goto_meter(Instruction instruction_goto_meter) {
        this.instruction_goto_meter = instruction_goto_meter;
    }

    public Instruction getInstruction_experimenter() {
        return instruction_experimenter;
    }

    public void setInstruction_experimenter(Instruction instruction_experimenter) {
        this.instruction_experimenter = instruction_experimenter;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.instruction_goto_table);
        hash = 11 * hash + Objects.hashCode(this.instruction_write_metadata);
        hash = 11 * hash + Objects.hashCode(this.instruction_write_actions);
        hash = 11 * hash + Objects.hashCode(this.instruction_apply_actions);
        hash = 11 * hash + Objects.hashCode(this.instruction_clear_actions);
        hash = 11 * hash + Objects.hashCode(this.instruction_goto_meter);
        hash = 11 * hash + Objects.hashCode(this.instruction_experimenter);
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
        final FlowInstructiondSets other = (FlowInstructiondSets) obj;
        if (!Objects.equals(this.instruction_goto_table, other.instruction_goto_table)) {
            return false;
        }
        if (!Objects.equals(this.instruction_write_metadata, other.instruction_write_metadata)) {
            return false;
        }
        if (!Objects.equals(this.instruction_write_actions, other.instruction_write_actions)) {
            return false;
        }
        if (!Objects.equals(this.instruction_apply_actions, other.instruction_apply_actions)) {
            return false;
        }
        if (!Objects.equals(this.instruction_clear_actions, other.instruction_clear_actions)) {
            return false;
        }
        if (!Objects.equals(this.instruction_goto_meter, other.instruction_goto_meter)) {
            return false;
        }
        if (!Objects.equals(this.instruction_experimenter, other.instruction_experimenter)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FlowInstructiondSets{" + "instruction_goto_table=" + instruction_goto_table + ", instruction_write_metadata=" + instruction_write_metadata + ", instruction_write_actions=" + instruction_write_actions + ", instruction_apply_actions=" + instruction_apply_actions + ", instruction_clear_actions=" + instruction_clear_actions + ", instruction_goto_meter=" + instruction_goto_meter + ", instruction_experimenter=" + instruction_experimenter + '}';
    }

}
