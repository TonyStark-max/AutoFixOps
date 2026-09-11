from pydantic import BaseModel, Field
from typing import List

class Diagnosis(BaseModel):
    root_cause: str = Field(description="A detailed explanation of the root cause.")
    suspected_files: List[str] = Field(description="List of files suspected to contain the bug.")
    evidence: str = Field(description="Evidence for the root cause based on logs.")
    confidence: int = Field(description="Confidence score (0-100) based on log-code correlation.")

class FilePatch(BaseModel):
    file_path: str = Field(description="The path to the file that needs to be fixed.")
    new_content: str = Field(description="The COMPLETE new content of the file after applying the fix.")

class PatchProposal(BaseModel):
    files: List[FilePatch] = Field(description="List of files to be modified or created to fix the issue.")
    explanation: str = Field(description="Explanation of why this patch is safe and correct.")

class RegressionTestProposal(BaseModel):
    test_code: str = Field(description="The regression test code to reproduce and verify the fix.")
    test_file_path: str = Field(description="The path where the test should be placed.")

class ReviewResult(BaseModel):
    approved: bool = Field(description="Whether the patch is approved for validation.")
    findings: str = Field(description="Any issues found in the patch during self-review.")
    risk_level: str = Field(description="Risk level: LOW, MEDIUM, HIGH, CRITICAL.")
