import os
import requests
from github import Github

class GitHubService:
    def __init__(self):
        self.token = os.getenv("GITHUB_TOKEN")
        
    def create_pr(self, repo_name: str, branch: str, title: str, body: str, patches: list) -> str:
        if not self.token or self.token == "your_real_github_token_here" or "your_github_token_here" in self.token:
            return f"https://github.com/{repo_name}/pulls/mock_id" # Mock
            
        try:
            g = Github(self.token)
            repo = g.get_repo(repo_name)
            
            # Determine default branch
            default_branch = repo.default_branch
            main_ref = repo.get_git_ref(f"heads/{default_branch}")
            
            # Create a new branch for the fix
            repo.create_git_ref(ref=f"refs/heads/{branch}", sha=main_ref.object.sha)
            
            # Apply all patches to the branch
            for patch in patches:
                file_path = patch.get("file_path")
                new_content = patch.get("new_content")
                
                try:
                    file_info = repo.get_contents(file_path, ref=default_branch)
                    repo.update_file(
                        path=file_path, 
                        message=f"AutoFixOps Patch for {file_path}", 
                        content=new_content, 
                        sha=file_info.sha, 
                        branch=branch
                    )
                except Exception as e:
                    # If file doesn't exist, create it (fallback)
                    repo.create_file(
                        path=file_path,
                        message=f"AutoFixOps Creation for {file_path}",
                        content=new_content,
                        branch=branch
                    )
                
            # Open Pull Request
            pr = repo.create_pull(
                title=title, 
                body=body, 
                head=branch, 
                base=default_branch
            )
            return pr.html_url
            
        except Exception as e:
            return f"https://github.com/{repo_name}/pulls/error_failed_to_create_pr_({str(e).replace(' ', '_')})"

class TelegramService:
    def __init__(self):
        self.token = os.getenv("TELEGRAM_BOT_TOKEN")
        self.chat_ids = os.getenv("TELEGRAM_AUTHORIZED_CHAT_IDS", "").split(",")
        
    def send_message(self, message: str):
        if not self.token or self.token == "your_real_telegram_bot_token_here" or "your_telegram_bot_token_here" in self.token:
            return # Mock
            
        for chat_id in self.chat_ids:
            if not chat_id.strip(): continue
            try:
                requests.post(
                    f"https://api.telegram.org/bot{self.token}/sendMessage",
                    json={"chat_id": chat_id, "text": message}
                )
            except Exception:
                pass
