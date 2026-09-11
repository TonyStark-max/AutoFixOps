from typing import TypedDict, Optional, List, Dict, Any

class AutoFixState(TypedDict, total=False):
    incident_id: str
    repository_id: str
    repository_url: str
    base_branch: str
    working_branch: str
    
    incident_source: str
    incident_timestamp: str
    
    raw_logs: str
    sanitized_logs: str
    stack_trace: str
    error_type: str
    error_message: str
    
    suspected_files: List[str]
    suspected_line_numbers: List[int]
    relevant_code_context: str
    
    root_cause: str
    root_cause_evidence: str
    root_cause_confidence: int
    
    generated_patch: str
    changed_files: List[str]
    generated_test: str
    
    build_result: str
    existing_test_result: str
    regression_test_result: str
    static_analysis_result: str
    
    blast_radius_score: int
    blast_radius_level: str
    
    review_result: str
    review_findings: str
    
    evidence_score: int
    fix_confidence: int
    risk_level: str
    
    retry_count: int
    
    pull_request_url: str
    
    current_stage: str
    
    failure_reason: str
