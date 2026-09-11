from abc import ABC, abstractmethod
from typing import Type, TypeVar, Any
from pydantic import BaseModel
import os
import google.generativeai as genai
import json

T = TypeVar('T', bound=BaseModel)

class LLMProvider(ABC):
    @abstractmethod
    def generate_structured(self, prompt: str, schema: Type[T]) -> T:
        pass
    
    @abstractmethod
    def generate_text(self, prompt: str) -> str:
        pass

class GeminiProvider(LLMProvider):
    def __init__(self):
        # We assume the user has set GEMINI_API_KEY in the environment if running locally.
        # Otherwise, the system environment provides it.
        api_key = os.getenv("GEMINI_API_KEY")
        if api_key:
            genai.configure(api_key=api_key)
        self.model_id = os.getenv("GEMINI_MODEL_ID", "gemini-2.5-flash")
        
    def generate_structured(self, prompt: str, schema: Type[T]) -> T:
        model = genai.GenerativeModel(self.model_id)
        # Using schema instructions for JSON output
        full_prompt = f"{prompt}\n\nPlease output valid JSON that conforms to the following JSON schema:\n{json.dumps(schema.model_json_schema())}"
        
        response = model.generate_content(
            full_prompt,
            generation_config=genai.types.GenerationConfig(
                response_mime_type="application/json"
            )
        )
        try:
            return schema.model_validate_json(response.text)
        except Exception as e:
            raise ValueError(f"Failed to parse structured output: {e}\nResponse was: {response.text}")

    def generate_text(self, prompt: str) -> str:
        model = genai.GenerativeModel(self.model_id)
        response = model.generate_content(prompt)
        return response.text
