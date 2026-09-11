import os
import subprocess
from typing import List, Dict

class RepoTools:
    def __init__(self, repo_path: str):
        self.repo_path = repo_path

    def list_repository_files(self) -> List[str]:
        result = subprocess.run(
            ["find", ".", "-type", "f", "-not", "-path", "*/.git/*", "-not", "-path", "*/target/*"],
            cwd=self.repo_path,
            capture_output=True,
            text=True
        )
        return result.stdout.strip().split("\n")

    def read_repository_file(self, file_path: str) -> str:
        safe_path = os.path.abspath(os.path.join(self.repo_path, file_path))
        if not safe_path.startswith(os.path.abspath(self.repo_path)):
            raise ValueError("Path traversal denied.")
            
        with open(safe_path, "r") as f:
            return f.read()

    def search_repository(self, keyword: str) -> List[str]:
        result = subprocess.run(
            ["grep", "-rnI", keyword, "."],
            cwd=self.repo_path,
            capture_output=True,
            text=True
        )
        return result.stdout.strip().split("\n")

    def apply_patch(self, file_path: str, new_content: str):
        safe_path = os.path.abspath(os.path.join(self.repo_path, file_path))
        if not safe_path.startswith(os.path.abspath(self.repo_path)):
            raise ValueError("Path traversal denied.")
            
        with open(safe_path, "w") as f:
            f.write(new_content)
