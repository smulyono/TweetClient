ALTER TABLE tweets ADD COLUMN retweeted INTEGER;

ALTER TABLE tweet_user ADD COLUMN tweets_count INTEGER;

ALTER TABLE tweetclient_user ADD COLUMN tweets_count INTEGER;
