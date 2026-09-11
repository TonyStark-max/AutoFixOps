# AutoFixOps

AutoFixOps is an Autonomous AI DevOps Agent designed to detect deployment crashes in real-time, autonomously diagnose root causes, generate multi-file code fixes, and submit Pull Requests to restore services with zero human intervention.

This project was built for DeVert-A-Thon'26.

---

## Architecture and Workflow

AutoFixOps leverages an Event-Driven Webhook Architecture combined with a LangGraph Cognitive State Machine. 

1. **Crash Detection:** A target application crashes during deployment on a hosting platform (e.g., Render, Railway).
2. **Webhook Trigger:** The deployment platform fires an HTTP Webhook containing the raw crash logs to the AutoFixOps Agent.
3. **AI Diagnosis:** The Agent uses Large Language Models (Gemini, OpenAI, or local Ollama) to analyze the logs and pinpoint the failing code.
4. **Multi-File Patching:** The AI generates precise code replacements for all broken files simultaneously.
5. **Safety Gate Review:** The AI conducts a security review of its own patch to ensure it does not introduce vulnerabilities, such as outdated dependencies.
6. **Autonomous Pull Request:** Using the GitHub API, the Agent forks a branch, safely commits the fixed files, and opens a Pull Request on the target repository.
7. **Human-in-the-Loop:** The Agent sends a direct message to a designated Telegram chat containing the PR link.
8. **Auto-Redeploy:** Once a developer clicks "Merge" on GitHub, the native hosting platform detects the merge and successfully redeploys the fixed application.

---

## Repository Structure & Context

To properly test and demonstrate the capabilities of this system, the workflow explicitly utilizes **two separate repositories**:

1. **The AutoFixOps Platform (This Repository):** 
   This codebase contains the AI Orchestrator and the backend database. You run this platform independently (either locally or on a separate cloud server) to actively monitor external applications.
2. **The Target Application (Dummy Repository):** 
   You must have a separate, buggy application repository (e.g., a simple Node.js application). **This dummy repository is what you actually deploy to the hosting platform (like Render).** When the dummy application fails to deploy on Render, Render is configured to send the crash logs via Webhook to the AutoFixOps Platform (this repository) to be analyzed and fixed.

---

## Setup and Installation

### Prerequisites
- Docker and Docker Compose installed on your system.
- Telegram Bot Token (obtained via BotFather).
- GitHub Personal Access Token (with repository write access).
- Gemini API Key (or OpenAI / Ollama alternative).

### 1. Environment Configuration (Crucial Step)
You must explicitly configure your environment variables before running the application. The system requires these credentials to interact with external APIs.

Clone this repository and create your `.env` file:
```bash
git clone https://github.com/TonyStark-max/AutoFixOps.git
cd AutoFixOps
cp .env.example .env
```

Open the newly created `.env` file in your preferred text editor. You must manually enter your specific credentials into this file. Here is the step-by-step breakdown of the required configuration:

- **Database Credentials:** Configure `DB_USER` and `DB_PASSWORD` for the PostgreSQL container.
- **GitHub Integration:** Enter your `GITHUB_TOKEN` to authorize the Agent to fork and create Pull Requests.
- **Telegram Notifications:** Input your `TELEGRAM_BOT_TOKEN` and `TELEGRAM_AUTHORIZED_CHAT_IDS` to receive approval messages on your phone.
- **AI Brain Setup:** Choose your `LLM_PROVIDER` (e.g., `gemini`, `openai`, `ollama`) and supply the corresponding API key (e.g., `GEMINI_API_KEY`).

### 2. Containerized Deployment
This entire platform is fully containerized. You do not need to manually install Java, Python, or PostgreSQL on your host machine.

Simply execute the following command in the root directory:
```bash
docker compose up --build
```
This single command will autonomously build the images, configure the networking, and simultaneously spin up the PostgreSQL Database, the Spring Boot Backend (Port 8080), and the FastAPI Agent (Port 8000).

---

## How to Test the End-to-End Workflow

There are two ways to test this application:

### Option A: The Simulated Hackathon Demo (Local Test)
If you want to instantly test the AI without deploying the dummy application to the cloud, you can simulate a Render crash locally.

- **Step 1: Start the Platform:** Ensure the AutoFixOps platform is running via `docker compose up --build`.
- **Step 2: Fire the Webhook:** A `payload.json` file is provided in the root directory containing multi-file crash logs. Run the following command from the root directory to inject the logs directly into your Agent:
  ```bash
  curl -X POST http://localhost:8000/api/webhook/render \
    -H "Content-Type: application/json" \
    -d @payload.json
  ```
- **Step 3: Monitor the Rescue:** Watch the Docker logs to observe the AI dissecting the error, generating the fixes, and pushing the PR to GitHub. Check your Telegram for the instant notification link.

### Option B: The Live Production Demo (Render/Railway)
To demonstrate the actual production workflow:

- **Step 1: Expose the Agent:** Ensure the AutoFixOps platform is running via Docker and exposed to the internet (using a tool like `ngrok` or deploying to a cloud VPS).
- **Step 2: Deploy Target App:** Deploy your secondary Dummy Repository to a platform like Render.
- **Step 3: Configure the Webhook:** You must explicitly tell Render where to send the crash logs:
  - Go to your Render Dashboard (`dashboard.render.com`).
  - Click on the Web Service running your Dummy App.
  - Navigate to the **Settings** tab.
  - Scroll down to the **Log Streams / Webhooks** section.
  - In the "URL" field, paste your Agent's public URL (e.g., `https://your-ngrok-url.com/api/webhook/render`).
- **Step 4: Trigger a Crash:** Force a broken commit to the Dummy Repository.
- **Step 5: Autonomous Rescue:** Render will fail to deploy and automatically fire the webhook. The Agent will analyze the logs, rewrite the code, open a GitHub Pull Request, and message your Telegram.
- **Step 6: Auto-Redeploy:** Click "Merge" on the generated Pull Request. Render will detect the merge and automatically trigger a successful redeployment.
