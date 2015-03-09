# Week 3 Project : Twitter Client

Build a simple Twitter client that supports viewing a Twitter timeline and composing a new tweet. The project
build based on the [RestClient](https://github.com/codepath/android-rest-client-template) template project

The API used :

- [Retrieve user twitter logged in user](https://dev.twitter.com/rest/reference/get/account/verify_credentials)
- [Retrieve home timeline](https://dev.twitter.com/rest/reference/get/statuses/home_timeline)
- [Compose new tweets](https://dev.twitter.com/rest/reference/post/statuses/update)

User story completed:

- [x] User can sign in to Twitter using OAuth login
- [x] User can view tweets from their home timeline
        
        - Display username, name, body, number of favourites, number of retweet, relative timestamp
        - Infinite scrolling / pagination
        - links in tweet are clickable
        
- [x] User can compose a tweet

        - Using navbar to allow user compose a tweet and post them directly into the timeline
        - User can see counter of number character available when posting a tweet
        
- [x] Refresh using pulling down to refresh
- [x] Open offline by retrieving data from DB
- [x] User can see __detail__ view of the weet
- [x] Compose activity is using modal overlay

![Walkthrough.gif](walkthrough.gif)