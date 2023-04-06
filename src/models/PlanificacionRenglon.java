package models;

public class PlanificacionRenglon {

    public Double cantPlanif;
    public Double costMatPlan;
    public Double costManoPlan;
    public Double costEquipPlan;
    public Double costSalaraio;

    public PlanificacionRenglon(Double cantPlanif, Double costMatPlan, Double costManoPlan, Double costEquipPlan, Double costSalaraio) {
        this.cantPlanif = cantPlanif;
        this.costMatPlan = costMatPlan;
        this.costManoPlan = costManoPlan;
        this.costEquipPlan = costEquipPlan;
        this.costSalaraio = costSalaraio;
    }

    public Double getCantPlanif() {
        return cantPlanif;
    }

    public void setCantPlanif(Double cantPlanif) {
        this.cantPlanif = cantPlanif;
    }

    public Double getCostMatPlan() {
        return costMatPlan;
    }

    public void setCostMatPlan(Double costMatPlan) {
        this.costMatPlan = costMatPlan;
    }

    public Double getCostManoPlan() {
        return costManoPlan;
    }

    public void setCostManoPlan(Double costManoPlan) {
        this.costManoPlan = costManoPlan;
    }

    public Double getCostEquipPlan() {
        return costEquipPlan;
    }

    public void setCostEquipPlan(Double costEquipPlan) {
        this.costEquipPlan = costEquipPlan;
    }

    public Double getCostSalaraio() {
        return costSalaraio;
    }

    public void setCostSalaraio(Double costSalaraio) {
        this.costSalaraio = costSalaraio;
    }
}
