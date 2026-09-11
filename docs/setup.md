# AutoFixOps Setup Guide

## Prerequisites

Ensure you have the following installed on your system:
- Java 21+
- Maven 3.9+
- Python 3.12+
- Docker
- Git
- AWS CLI
- PostgreSQL

## Configuration

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Fill in the required environment variables:
   - `GITHUB_TOKEN`: A GitHub Personal Access Token with repo access.
   - `AWS_REGION`: The AWS region where Amazon Bedrock is available (e.g., `us-east-1` or `us-west-2`).
   - `TELEGRAM_BOT_TOKEN`: A bot token obtained from BotFather on Telegram.

## Bedrock Access

1. Ensure your AWS account has access to the **Anthropic Claude Sonnet** model in Amazon Bedrock.
2. If running locally, authenticate your AWS CLI:
   ```bash
   aws configure
   ```
   Or use AWS SSO.

## Database Setup

1. Create a PostgreSQL database and user corresponding to the `.env` configuration.
2. Flyway will automatically run migrations upon application startup.
