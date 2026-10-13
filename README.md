# one-health
An interactive knowledge base for bioactive compound discovery


## ORCID Login Configuration

To enable ORCID authentication, configure the required environment variables.

### 1. Create the environment file

Copy the existing `.env_template` file to `.env` in the project root directory:

```bash
cp .env_template .env
```

### 2. Configure ORCID credentials

Open the `.env` file and update the following variables:

```dotenv
ORCID_CLIENT_ID="please-insert-data"
ORCID_CLIENT_SECRET="please-insert-data"
ORCID_REDIRECT_URI="please-insert-data"
```

Replace the placeholders with your ORCID application credentials:

- **ORCID_CLIENT_ID**: The client ID of your registered ORCID application.
- **ORCID_CLIENT_SECRET**: The client secret associated with your ORCID application.
- **ORCID_REDIRECT_URI**: The callback URL configured in your ORCID application settings. It must match the registered redirect URI.

Credentials can be obtained from the [ORCID Developer Tools](https://orcid.org/developer-tools).

### 3. Security

The `.env` file contains sensitive credentials and must not be committed to Git. Only `.env_template` with placeholder values should be tracked in the repository.

Verify that `.env` is excluded from Git tracking:

```bash
git check-ignore .env
```

If the command outputs `.env`, the file is ignored by Git. If it produces no output, add `.env` to `.gitignore`.