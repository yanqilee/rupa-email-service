# Rupa Health Transactional Email Service

## Setup
___
To use the app, first add your Mailgun or Sendgrid API keys to `src/main/resources/application.properties`. The default email service is Sendgrid,
that can be changed by changing the `email_service` properties in `application.properties` to `mailgun`.

To run the app, in the root folder, run command `./mvnw spring-boot:run`.

The app will run on http://localhost:8080, the email API is on http://localhost:8080/email

## API Docs
___
Request:
- `to`: the email address to send to, must not be null, must be in valid email format
- `to_name`: the name to accompany the email, must not be null, must be between 1 and 30 characters long
- `from`: the email address in the from and reply fields, must not be null, must be in valid email format
- `from_name`: the name to accompany the from/reply emails, must not be null, must be between 1 and 30 characters long
- `subject`: the subject line of the email, must not be null, must be between 1 and 100 characters long
- `body`: the HTML body of the email, must not be null, must be between 1 and 500 characters long

Response:
- `statusCode`: HTTP status code in response to the request
- `errorMessage`: present if there is an error in processing the request, a plaintext message signaling the source of failure

Sample request:
```
{
    "to": "fake@example.com",
    "to_name": "Mr. Fake",
    "from": "no-reply@fake.com",
    "from_name":"Ms. Fake",
    "subject": "A message from The Fake Family",
    "body": "<h1>Your Bill</h1><p>$10</p>"
}
```
Sample success response:
```
{
    "statusCode": 200,
}
```
Sample failure response:
```
{
    "statusCode": 500,
    "errorMessage": "Error while calling email client."
}
```

## Technology Choice
___
I chose Spring Boot because it is a powerful framework for building RESTful APIs. I've used libraries such as validator and HTML sanitizer to check the input.
There are all technologies I'm familiar with. 

With limited time to spend on this exercise, this setup allowed me to skip out on a lot of boilerplate code that would have taken me a non-trivial amount of time.


## Tradeoffs and Additional Information
___
For this exercise, I know I have limited free time to work on this.
I've decided to prioritize on getting the functionalities to work, particularly, making successful calls to Mailgun and Sendgrid,
making unit tests something nice to have at the end if I have extra time.

I know Mailgun/Sendgrid calls were the only unknown part of this exercise for me. Once I've signed up for accounts, successfully
made HTTP calls to these services and verified I've received the email, I can finish up the other parts fairly quick.

I have some simple test cases I've added along the way, unfortunately, I didn't have enough time to write unit tests to
fully cover the entire application. I've spent roughly 4 hours on this exercise.

I have thought about abstracting away the email client implementation itself, having the app
be agnostic to what service it calls, since it is just hitting an HTTP endpoint with an API key.
Upon looking into Mailgun and Sendgrid HTTP request format, they are quite different and requires mapping
in their own ways. I've decided to create separate client classes for Mailgun and Sendgrid.

I have also created conditional beans for the two email clients (Mailgun and Sendgrid), and used a property
value to toggle between them. It's pretty painless to implement it this way, but 
to switch email clients, a redeployment is needed. If I have more time, I would look into
a way to hot swap the clients, maybe even auto fail-over in case one client is down.

In addition to what I mentioned above, If I had extra time, here are some thoughts on possible improvements:
- Error status/message mapping, I have very primitive error handling in this app. Mailgun and Sendgrid do have good error mappings in their documentation. This way we can send back more detailed response to the consumers of this API
- Secret storage for API key, possibly integrating with AWS Secrets Manager or something equivalent. Right now it is just in the properties file, I had to leave myself a note to remove my API keys before committing for this exercise
- Authentication to use the email API, so it is not open to everyone
- Retries in case we have trouble reaching the email clients (there is already retries from email clients themselves)
- Allowing multiple receivers (multiple people in the `to` field), currently it is hardcoded to only one receiver
