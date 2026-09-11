import subprocess
import os

class SandboxRunner:
    def __init__(self, repo_path: str):
        self.repo_path = repo_path

    def run_tests(self) -> bool:
        # In a real environment, we'd use CodeBuild or Docker-in-Docker.
        # For this MVP, we execute maven wrapper in the cloned repo.
        try:
            result = subprocess.run(
                ["./mvnw", "clean", "test"],
                cwd=self.repo_path,
                capture_output=True,
                text=True,
                timeout=60
            )
            return result.returncode == 0
        except Exception:
            return False
