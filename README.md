# Run application

- Fill .env file with your smtp server properties
- Open terminal in project's root directory
- `./mvnw clean package -DskipTests=true`
- `docker-compose up`

Make POST to http://localhost:8080/api/emails with body

```
{
    "subject": "email subject",
    "content": "email content",
    "emailsTo": [
        "your_email1@gmail.com"
    ]
}
```