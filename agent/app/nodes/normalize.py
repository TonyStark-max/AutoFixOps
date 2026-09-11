from app.models.state import AutoFixState
from langgraph.graph import StateGraph, END
import re

def normalize_incident(state: AutoFixState) -> AutoFixState:
    state["current_stage"] = "NORMALIZING"
    
    # Strip dangerous shell characters or extremely long payloads
    error_message = state.get("error_message", "")
    sanitized = re.sub(r'[;$&|`<>]', '', error_message)
    sanitized = sanitized[:2000] # Limit size
    
    state["sanitized_logs"] = sanitized
    
    # Very basic normalization logic for demo
    if "NoSuchElementException" in sanitized or "Optional.get()" in sanitized:
        state["error_type"] = "Runtime Failure"
    elif "CRASH" in sanitized or "TARGET_ENV" in sanitized:
        state["error_type"] = "Configuration Failure"
    elif "does not exist" in sanitized or "PSQLException" in sanitized:
        state["error_type"] = "SQL Failure"
        
    return state
