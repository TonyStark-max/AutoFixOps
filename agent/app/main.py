from fastapi import FastAPI
from pydantic import BaseModel
from app.graph.orchestrator import create_graph
from app.models.state import AutoFixState
from dotenv import load_dotenv

load_dotenv()

app = FastAPI(title="AutoFixOps Agent Service")
graph = create_graph()

class AnalyzeRequest(BaseModel):
    incident_id: str
    error_type: str
    error_message: str

@app.post("/api/analyze")
async def analyze_incident(req: AnalyzeRequest):
    state = AutoFixState(
        incident_id=req.incident_id,
        error_type=req.error_type,
        error_message=req.error_message
    )
    
    result = graph.invoke(state)
    return {"status": "success", "result": result}

class RenderWebhookPayload(BaseModel):
    # This is a simplified schema for a platform like Render or Railway
    serviceId: str
    type: str  # e.g., 'deploy.failure'
    log_snippet: str

@app.post("/api/webhook/render")
async def render_webhook(payload: RenderWebhookPayload):
    # This endpoint receives the raw logs from Render/Railway when a deployment fails
    state = AutoFixState(
        incident_id=f"RENDER_{payload.serviceId}",
        error_type="Deployment Failure",
        error_message=payload.log_snippet
    )
    
    # Automatically triggers the LangGraph AI to analyze the deployment log
    result = graph.invoke(state)
    return {"status": "success", "message": "Render logs analyzed and fix proposed.", "result": result}

@app.get("/")
async def root():
    return {"message": "AutoFixOps Agent Service is running. Access /docs for API documentation."}
