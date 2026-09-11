from langgraph.graph import StateGraph, END
from app.models.state import AutoFixState
from app.nodes.normalize import normalize_incident
from app.nodes.analyze import analyze_logs
from app.nodes.diagnose import diagnose_root_cause
from app.nodes.workflows import (
    generate_patch, generate_regression_test, run_sandbox, 
    calculate_blast_radius, self_review, policy_gate
)

def create_graph():
    workflow = StateGraph(AutoFixState)
    
    workflow.add_node("normalize_incident", normalize_incident)
    workflow.add_node("analyze_logs", analyze_logs)
    workflow.add_node("diagnose_root_cause", diagnose_root_cause)
    workflow.add_node("generate_patch", generate_patch)
    workflow.add_node("generate_regression_test", generate_regression_test)
    workflow.add_node("run_sandbox", run_sandbox)
    workflow.add_node("calculate_blast_radius", calculate_blast_radius)
    workflow.add_node("self_review", self_review)
    workflow.add_node("policy_gate", policy_gate)
    
    workflow.set_entry_point("normalize_incident")
    workflow.add_edge("normalize_incident", "analyze_logs")
    workflow.add_edge("analyze_logs", "diagnose_root_cause")
    workflow.add_edge("diagnose_root_cause", "generate_patch")
    workflow.add_edge("generate_patch", "generate_regression_test")
    workflow.add_edge("generate_regression_test", "run_sandbox")
    workflow.add_edge("run_sandbox", "calculate_blast_radius")
    workflow.add_edge("calculate_blast_radius", "self_review")
    workflow.add_edge("self_review", "policy_gate")
    workflow.add_edge("policy_gate", END)
    
    return workflow.compile()
